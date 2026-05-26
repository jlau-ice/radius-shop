package com.ice.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.common.constant.OrderStatus;
import com.ice.common.exception.BusinessException;
import com.ice.entity.*;
import com.ice.mapper.OrderItemMapper;
import com.ice.mapper.OrderMapper;
import com.ice.utils.WxPayService;
import com.ice.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMapper, Order> {
    private final OrderItemMapper orderItemMapper;
    private final ProductService productService;
    private final CartService cartService;
    private final AddressService addressService;
    private final WxPayService wxPayService;
    private final UserService userService;

    @Transactional
    public Order submit(Long userId, Long addressId, String remark) {
        AddressTemplate addr = addressService.getActiveById(addressId);
        List<CartItem> checked = cartService.lambdaQuery()
                .eq(CartItem::getUserId, userId)
                .eq(CartItem::getChecked, 1).list();
        if (checked.isEmpty()) {
            throw new BusinessException("请选择商品");
        }

        Order order = new Order();
        order.setOrderNo(IdUtil.getSnowflakeNextIdStr());
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryAddress(addr.getAddress());
        order.setDeliveryContact(addr.getContactName());
        order.setDeliveryPhone(addr.getContactPhone());
        order.setRemark(remark);

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : checked) {
            Product p = productService.getById(ci.getProductId());
            if (p == null || p.getStatus() == 0) {
                throw new BusinessException(p != null ? p.getName() + " 已下架" : "商品已下架");
            }
            if (p.getStock() < ci.getQuantity()) {
                throw new BusinessException(p.getName() + " 库存不足，当前库存: " + p.getStock());
            }
            total = total.add(p.getPrice().multiply(new BigDecimal(ci.getQuantity())));
        }
        order.setTotalAmount(total);
        order.setPayAmount(total);
        save(order);

        for (CartItem ci : checked) {
            Product p = productService.getById(ci.getProductId());
            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(p.getId());
            item.setProductName(p.getName());
            item.setProductImage(p.getCoverImage());
            item.setSkuInfo(ci.getSkuInfo());
            item.setPrice(p.getPrice());
            item.setQuantity(ci.getQuantity());
            item.setAmount(p.getPrice().multiply(new BigDecimal(ci.getQuantity())));
            orderItemMapper.insert(item);

            productService.deductStock(p.getId(), ci.getQuantity());
        }

        cartService.removeBatch(userId, checked.stream().map(CartItem::getId).collect(Collectors.toList()));
        return order;
    }

    public Object createPayment(Long orderId, Long userId) {
        Order order = getOwnedOrder(orderId, userId);
        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new BusinessException("订单状态不正确");
        }
        User user = userService.getById(userId);
        String openId = user != null ? user.getOpenId() : "";
        return wxPayService.createPay(order, openId);
    }

    @Transactional
    public void handlePayNotify(String orderNo, String transactionId) {
        Order order = lambdaQuery().eq(Order::getOrderNo, orderNo).one();
        if (order == null) return;
        if (OrderStatus.PENDING.equals(order.getStatus())) {
            order.setStatus(OrderStatus.PAID);
            order.setPayTime(LocalDateTime.now());
            order.setPayTransactionId(transactionId);
            updateById(order);
        }
    }

    public OrderVO detailByUser(Long userId, Long orderId) {
        return toVO(getOwnedOrder(orderId, userId));
    }

    public OrderVO detailForAdmin(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        return toVO(order);
    }

    @Transactional
    public void startPreparing(Long orderId) {
        Order order = getById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (!OrderStatus.PAID.equals(order.getStatus())) {
            throw new BusinessException("只有已支付订单可以开始制作");
        }
        order.setStatus(OrderStatus.PREPARING);
        updateById(order);
    }

    public OrderVO toVO(Order o) {
        List<OrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, o.getId()));

        OrderVO.OrderVOBuilder builder = OrderVO.builder()
                .id(o.getId()).orderNo(o.getOrderNo()).status(o.getStatus())
                .totalAmount(o.getTotalAmount()).payAmount(o.getPayAmount())
                .deliveryAddress(o.getDeliveryAddress())
                .deliveryContact(o.getDeliveryContact()).deliveryPhone(o.getDeliveryPhone())
                .deliveryPhoto(o.getDeliveryPhoto()).remark(o.getRemark())
                .payTime(o.getPayTime()).deliveryTime(o.getDeliveryTime()).createTime(o.getCreateTime())
                .items(items.stream().map(i -> OrderItemVO.builder()
                        .productId(i.getProductId()).productName(i.getProductName())
                        .productImage(i.getProductImage()).skuInfo(i.getSkuInfo())
                        .price(i.getPrice()).quantity(i.getQuantity()).amount(i.getAmount())
                        .build()).collect(Collectors.toList()));

        return builder.build();
    }

    public PageVO<OrderVO> pageByUser(Long userId, int pageNum, int pageSize, String status) {
        if (status != null && !status.isBlank() && !OrderStatus.isValid(status)) {
            throw new BusinessException("订单状态不正确");
        }
        IPage<Order> page = lambdaQuery()
                .eq(Order::getUserId, userId)
                .eq(status != null && !status.isBlank(), Order::getStatus, status)
                .orderByDesc(Order::getCreateTime)
                .page(new Page<>(pageNum, pageSize));
        return PageVO.<OrderVO>builder()
                .total(page.getTotal()).pages(page.getPages()).current(pageNum)
                .records(page.getRecords().stream().map(this::toVO).collect(Collectors.toList()))
                .build();
    }

    @Transactional
    public void mockPay(Long userId, Long orderId) {
        Order order = getOwnedOrder(orderId, userId);
        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new BusinessException("订单状态不正确");
        }
        order.setStatus(OrderStatus.PAID);
        order.setPayTime(LocalDateTime.now());
        order.setPayTransactionId("mock_" + IdUtil.fastSimpleUUID());
        updateById(order);
    }

    @Transactional
    public void cancel(Long userId, Long orderId) {
        Order order = getOwnedOrder(orderId, userId);
        if (!OrderStatus.PENDING.equals(order.getStatus())) {
            throw new BusinessException("只能取消待支付订单");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReason("用户取消");
        updateById(order);
    }

    private Order getOwnedOrder(Long orderId, Long userId) {
        Order order = getById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        return order;
    }
}

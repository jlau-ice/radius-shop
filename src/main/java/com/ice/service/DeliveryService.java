package com.ice.service;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.common.constant.OrderStatus;
import com.ice.common.exception.BusinessException;
import com.ice.entity.DeliveryPerson;
import com.ice.entity.Order;
import com.ice.mapper.DeliveryPersonMapper;
import com.ice.mapper.OrderMapper;
import com.ice.service.impl.CosService;
import com.ice.vo.DeliveryInfoVO;
import com.ice.vo.OrderVO;
import com.ice.vo.PageVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import static cn.dev33.satoken.stp.StpUtil.getLoginIdAsLong;

@Service
@RequiredArgsConstructor
public class DeliveryService extends ServiceImpl<DeliveryPersonMapper, DeliveryPerson> {
    private final OrderMapper orderMapper;
    private final OrderService orderService;
    private final CosService cosService;
    @Value("${app.mock.delivery-login:true}")
    private boolean mockDeliveryLogin;

    public String login(String username, String password) {
        if (mockDeliveryLogin) {
            cn.dev33.satoken.stp.StpUtil.login(1L);
            return cn.dev33.satoken.stp.StpUtil.getTokenValue();
        }
        DeliveryPerson dp = lambdaQuery()
                .eq(DeliveryPerson::getUsername, username)
                .eq(DeliveryPerson::getStatus, 1).one();
        if (dp == null || !BCrypt.checkpw(password, dp.getPassword())) {
            throw new BusinessException("账号或密码错误");
        }
        cn.dev33.satoken.stp.StpUtil.login(dp.getId());
        return cn.dev33.satoken.stp.StpUtil.getTokenValue();
    }

    public Long currentId() {
        return getLoginIdAsLong();
    }

    public PageVO<OrderVO> pendingOrders(int page, int size) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getStatus, OrderStatus.PREPARING)
                .isNull(Order::getDeliveryPersonId)
                .orderByAsc(Order::getCreateTime);
        IPage<Order> pg = orderMapper.selectPage(new Page<>(page, size), wrapper);
        return wrap(pg, page);
    }

    public PageVO<OrderVO> myDeliveringOrders(int page, int size) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getDeliveryPersonId, currentId())
                .eq(Order::getStatus, OrderStatus.DELIVERING)
                .orderByDesc(Order::getCreateTime);
        IPage<Order> pg = orderMapper.selectPage(new Page<>(page, size), wrapper);
        return wrap(pg, page);
    }

    public PageVO<OrderVO> myDoneOrders(int page, int size) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<Order>()
                .eq(Order::getDeliveryPersonId, currentId())
                .eq(Order::getStatus, OrderStatus.DELIVERED)
                .orderByDesc(Order::getCreateTime);
        IPage<Order> pg = orderMapper.selectPage(new Page<>(page, size), wrapper);
        return wrap(pg, page);
    }

    @Transactional
    public void accept(Long orderId) {
        int updated = orderMapper.update(null, new LambdaUpdateWrapper<Order>()
                .eq(Order::getId, orderId)
                .eq(Order::getStatus, OrderStatus.PREPARING)
                .isNull(Order::getDeliveryPersonId)
                .set(Order::getDeliveryPersonId, currentId())
                .set(Order::getStatus, OrderStatus.DELIVERING));
        if (updated == 0) {
            throw new BusinessException("该订单已被接单或状态不可配送");
        }
    }

    @Transactional
    public void complete(Long orderId, MultipartFile photo) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getDeliveryPersonId() == null || !order.getDeliveryPersonId().equals(currentId())) {
            throw new BusinessException("无权操作");
        }
        if (!OrderStatus.DELIVERING.equals(order.getStatus())) {
            throw new BusinessException("订单状态不可完成");
        }
        String url = cosService.upload(photo);
        order.setStatus(OrderStatus.DELIVERED);
        order.setDeliveryPhoto(url);
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    public OrderVO detail(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        if (order.getDeliveryPersonId() != null && !order.getDeliveryPersonId().equals(currentId())) {
            throw new BusinessException("无权查看");
        }
        return orderService.toVO(order);
    }

    private PageVO<OrderVO> wrap(IPage<Order> pg, int page) {
        return PageVO.<OrderVO>builder()
                .total(pg.getTotal()).pages(pg.getPages()).current(page)
                .records(pg.getRecords().stream().map(o -> {
                    OrderVO vo = orderService.toVO(o);
                    if (o.getDeliveryPersonId() != null) {
                        DeliveryPerson dp = getById(o.getDeliveryPersonId());
                        if (dp != null) {
                            vo.setDeliveryInfo(DeliveryInfoVO.builder()
                                    .personId(dp.getId()).personName(dp.getName())
                                    .personPhone(dp.getPhone()).build());
                        }
                    }
                    return vo;
                }).collect(Collectors.toList()))
                .build();
    }
}

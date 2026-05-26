"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "order",
  setup(__props) {
    const activeTab = common_vendor.ref("");
    const orders = common_vendor.ref([]);
    const statusTabs = [
      { label: "全部", value: "" },
      { label: "待支付", value: "pending" },
      { label: "制作中", value: "preparing" },
      { label: "配送中", value: "delivering" },
      { label: "已完成", value: "delivered" }
    ];
    const statusMap = { pending: "待支付", paid: "已支付", preparing: "制作中", delivering: "配送中", delivered: "已送达", cancelled: "已取消" };
    const colorMap = { pending: "#bd8b3b", paid: "#1f5a46", preparing: "#1f5a46", delivering: "#1f5a46", delivered: "#587763", cancelled: "#999" };
    function statusText(s) {
      return statusMap[s] || s;
    }
    function statusColor(s) {
      return colorMap[s] || "#999";
    }
    common_vendor.onShow(() => {
      const storedStatus = common_vendor.index.getStorageSync("orderStatusFilter");
      if (storedStatus !== void 0) {
        activeTab.value = storedStatus;
        common_vendor.index.removeStorageSync("orderStatusFilter");
      }
      fetchOrders();
    });
    async function fetchOrders() {
      try {
        const data = await api_index.getOrderPage({ page: 1, pageSize: 50, status: activeTab.value || void 0 });
        orders.value = data.records || [];
      } catch {
      }
    }
    function switchTab(v) {
      activeTab.value = v;
      fetchOrders();
    }
    function goDetail(id) {
      common_vendor.index.navigateTo({ url: `/pages/order-detail/order-detail?id=${id}` });
    }
    async function doCancel(id) {
      try {
        await api_index.cancelOrder(id);
        common_vendor.index.showToast({ title: "已取消" });
        fetchOrders();
      } catch {
      }
    }
    async function doPay(id) {
      try {
        const payData = await api_index.createPayment(id);
        if (payData.mock) {
          await api_index.mockPayOrder(id);
          common_vendor.index.showToast({ title: "测试支付成功" });
          fetchOrders();
          return;
        }
        common_vendor.index.requestPayment({
          provider: "wxpay",
          timeStamp: payData.timeStamp,
          nonceStr: payData.nonceStr,
          package: payData.package,
          signType: payData.signType,
          paySign: payData.paySign,
          success() {
            common_vendor.index.showToast({ title: "支付成功" });
            fetchOrders();
          },
          fail() {
            common_vendor.index.showToast({ title: "支付失败", icon: "none" });
          }
        });
      } catch {
      }
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(statusTabs, (s, k0, i0) => {
          return {
            a: common_vendor.t(s.label),
            b: activeTab.value === s.value ? 1 : "",
            c: s.value,
            d: common_vendor.o(($event) => switchTab(s.value), s.value)
          };
        }),
        b: common_vendor.f(orders.value, (order, k0, i0) => {
          var _a;
          return common_vendor.e({
            a: common_vendor.t(order.orderNo),
            b: common_vendor.t(statusText(order.status)),
            c: statusColor(order.status),
            d: common_vendor.f(order.items, (item, k1, i1) => {
              return {
                a: item.productImage,
                b: common_vendor.t(item.productName),
                c: common_vendor.t(item.price),
                d: common_vendor.t(item.quantity),
                e: item.productId
              };
            }),
            e: common_vendor.t(((_a = order.items) == null ? void 0 : _a.length) || 0),
            f: common_vendor.t(order.payAmount),
            g: order.status === "pending"
          }, order.status === "pending" ? {
            h: common_vendor.o(($event) => doCancel(order.id), order.id),
            i: common_vendor.o(($event) => doPay(order.id), order.id)
          } : {}, {
            j: order.id,
            k: common_vendor.o(($event) => goDetail(order.id), order.id)
          });
        }),
        c: orders.value.length === 0
      }, orders.value.length === 0 ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-93207a4f"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order/order.js.map

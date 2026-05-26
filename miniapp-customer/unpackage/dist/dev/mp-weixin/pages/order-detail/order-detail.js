"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "order-detail",
  setup(__props) {
    const order = common_vendor.ref(null);
    const statusMap = { pending: "待支付", paid: "已支付", preparing: "制作中", delivering: "配送中", delivered: "已送达", cancelled: "已取消" };
    function statusText(s) {
      return statusMap[s] || s;
    }
    common_vendor.onMounted(async () => {
      var _a;
      const pages = getCurrentPages();
      const query = ((_a = pages[pages.length - 1].$page) == null ? void 0 : _a.options) || {};
      try {
        order.value = await api_index.getOrderDetail(query.id);
      } catch {
      }
    });
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: order.value
      }, order.value ? common_vendor.e({
        b: common_vendor.t(statusText(order.value.status)),
        c: order.value.status === "pending"
      }, order.value.status === "pending" ? {} : {}, {
        d: order.value.status === "delivering"
      }, order.value.status === "delivering" ? {} : {}, {
        e: common_vendor.n(order.value.status),
        f: common_vendor.t(order.value.deliveryContact),
        g: common_vendor.t(order.value.deliveryPhone),
        h: common_vendor.t(order.value.deliveryAddress),
        i: order.value.deliveryInfo
      }, order.value.deliveryInfo ? {
        j: common_vendor.t(order.value.deliveryInfo.personName),
        k: common_vendor.t(order.value.deliveryInfo.personPhone)
      } : {}, {
        l: common_vendor.f(order.value.items, (item, k0, i0) => {
          return {
            a: item.productImage,
            b: common_vendor.t(item.productName),
            c: common_vendor.t(item.quantity),
            d: common_vendor.t(item.amount),
            e: item.productId
          };
        }),
        m: common_vendor.t(order.value.orderNo),
        n: common_vendor.t(order.value.createTime),
        o: common_vendor.t(order.value.payTime || "未支付"),
        p: common_vendor.t(order.value.remark || "无"),
        q: order.value.deliveryPhoto
      }, order.value.deliveryPhoto ? {
        r: order.value.deliveryPhoto
      } : {}, {
        s: common_vendor.t(order.value.payAmount)
      }) : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-71729483"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order-detail/order-detail.js.map

"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "order-detail",
  setup(__props) {
    const order = common_vendor.ref(null);
    common_vendor.onMounted(async () => {
      var _a;
      const pages = getCurrentPages();
      const query = ((_a = pages[pages.length - 1].$page) == null ? void 0 : _a.options) || {};
      try {
        order.value = await api_index.getOrderDetail(query.id);
      } catch {
      }
    });
    async function doAccept() {
      try {
        await api_index.acceptOrder(order.value.id);
        common_vendor.index.showToast({ title: "接单成功" });
        order.value.status = "delivering";
      } catch {
      }
    }
    function goComplete() {
      common_vendor.index.navigateTo({ url: `/pages/complete/complete?id=${order.value.id}` });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: order.value
      }, order.value ? common_vendor.e({
        b: common_vendor.t(order.value.deliveryContact),
        c: common_vendor.t(order.value.deliveryPhone),
        d: common_vendor.t(order.value.deliveryAddress),
        e: common_vendor.f(order.value.items, (item, k0, i0) => {
          return {
            a: common_vendor.t(item.productName),
            b: common_vendor.t(item.quantity),
            c: item.productId
          };
        }),
        f: order.value.remark
      }, order.value.remark ? {
        g: common_vendor.t(order.value.remark)
      } : {}, {
        h: order.value.status === "delivering"
      }, order.value.status === "delivering" ? {
        i: common_vendor.o(goComplete, "58")
      } : order.value.status === "paid" || order.value.status === "preparing" ? {
        k: common_vendor.o(doAccept, "99")
      } : {}, {
        j: order.value.status === "paid" || order.value.status === "preparing"
      }) : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-71729483"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/order-detail/order-detail.js.map

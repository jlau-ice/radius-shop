"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "records",
  setup(__props) {
    const orders = common_vendor.ref([]);
    const loading = common_vendor.ref(false);
    common_vendor.onMounted(() => fetchData());
    async function fetchData() {
      loading.value = true;
      try {
        const data = await api_index.getDoneOrders();
        orders.value = (data == null ? void 0 : data.records) || [];
      } catch {
      } finally {
        loading.value = false;
      }
    }
    function goDetail(id) {
      common_vendor.index.navigateTo({ url: `/pages/order-detail/order-detail?id=${id}` });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(orders.value, (order, k0, i0) => {
          return {
            a: common_vendor.t(order.orderNo),
            b: common_vendor.t(order.deliveryTime || order.createTime),
            c: common_vendor.t(order.deliveryAddress),
            d: common_vendor.t(order.deliveryContact),
            e: common_vendor.t(order.deliveryPhone),
            f: common_vendor.f(order.items, (item, i, i1) => {
              return {
                a: common_vendor.t(item.productName),
                b: common_vendor.t(item.quantity),
                c: common_vendor.t(i < order.items.length - 1 ? "、" : ""),
                d: i
              };
            }),
            g: common_vendor.t(order.payAmount),
            h: order.id,
            i: common_vendor.o(($event) => goDetail(order.id), order.id)
          };
        }),
        b: orders.value.length === 0 && !loading.value
      }, orders.value.length === 0 && !loading.value ? {} : {}, {
        c: loading.value
      }, loading.value ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-cb371200"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/records/records.js.map

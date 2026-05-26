"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const activeTab = common_vendor.ref("pending");
    const orders = common_vendor.ref([]);
    const loading = common_vendor.ref(false);
    const emptyText = common_vendor.computed(() => activeTab.value === "pending" ? "暂无待接单订单" : "暂无配送中订单");
    common_vendor.onShow(() => fetchData());
    async function fetchData() {
      loading.value = true;
      try {
        const data = activeTab.value === "pending" ? await api_index.getPendingOrders() : await api_index.getDeliveringOrders();
        orders.value = (data == null ? void 0 : data.records) || [];
      } catch {
      } finally {
        loading.value = false;
      }
    }
    function switchTab(tab) {
      if (activeTab.value === tab)
        return;
      activeTab.value = tab;
      fetchData();
    }
    async function doAccept(id) {
      try {
        await api_index.acceptOrder(id);
        common_vendor.index.showToast({ title: "接单成功" });
        activeTab.value = "delivering";
        fetchData();
      } catch {
      }
    }
    function goDetail(id) {
      common_vendor.index.navigateTo({ url: `/pages/order-detail/order-detail?id=${id}` });
    }
    function goComplete(id) {
      common_vendor.index.navigateTo({ url: `/pages/complete/complete?id=${id}` });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: activeTab.value === "pending" ? 1 : "",
        b: common_vendor.o(($event) => switchTab("pending"), "53"),
        c: activeTab.value === "delivering" ? 1 : "",
        d: common_vendor.o(($event) => switchTab("delivering"), "0c"),
        e: common_vendor.f(orders.value, (order, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(order.orderNo),
            b: common_vendor.t(order.createTime),
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
            g: common_vendor.t(order.payAmount)
          }, activeTab.value === "pending" ? {
            h: common_vendor.o(($event) => doAccept(order.id), order.id)
          } : {
            i: common_vendor.o(($event) => goComplete(order.id), order.id)
          }, {
            j: order.id,
            k: common_vendor.o(($event) => goDetail(order.id), order.id)
          });
        }),
        f: activeTab.value === "pending",
        g: orders.value.length === 0 && !loading.value
      }, orders.value.length === 0 && !loading.value ? {
        h: common_vendor.t(emptyText.value)
      } : {}, {
        i: loading.value
      }, loading.value ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1cf27b2a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map

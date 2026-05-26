"use strict";
const common_vendor = require("../../common/vendor.js");
const store_cart = require("../../store/cart.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "checkout",
  setup(__props) {
    const cartStore = store_cart.useCartStore();
    const selectedAddress = common_vendor.ref(null);
    const remark = common_vendor.ref("");
    const checkedItems = common_vendor.computed(() => cartStore.items.filter((i) => i.checked));
    const totalPrice = common_vendor.computed(() => {
      return checkedItems.value.reduce((s, i) => s + i.price * i.quantity, 0).toFixed(2);
    });
    common_vendor.onShow(() => {
      const pages = getCurrentPages();
      const current = pages[pages.length - 1];
      if (current == null ? void 0 : current.__exposeAddr)
        selectedAddress.value = current.__exposeAddr;
    });
    function goSelectAddress() {
      common_vendor.index.navigateTo({ url: "/pages/address/address" });
    }
    async function doSubmit() {
      if (!selectedAddress.value) {
        common_vendor.index.showToast({ title: "请选择配送地址", icon: "none" });
        return;
      }
      try {
        const order = await api_index.submitOrder({ addressId: selectedAddress.value.id, remark: remark.value });
        const payData = await api_index.createPayment(order.id);
        if (payData.mock) {
          await api_index.mockPayOrder(order.id);
          common_vendor.index.showToast({ title: "测试支付成功" });
          common_vendor.index.redirectTo({ url: "/pages/order/order" });
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
            common_vendor.index.redirectTo({ url: "/pages/order/order" });
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
        a: selectedAddress.value
      }, selectedAddress.value ? {
        b: common_vendor.t(selectedAddress.value.name),
        c: common_vendor.t(selectedAddress.value.contactPhone),
        d: common_vendor.t(selectedAddress.value.address)
      } : {}, {
        e: common_vendor.o(goSelectAddress, "0a"),
        f: common_vendor.f(checkedItems.value, (item, k0, i0) => {
          return common_vendor.e({
            a: item.coverImage,
            b: common_vendor.t(item.productName),
            c: item.skuInfo
          }, item.skuInfo ? {
            d: common_vendor.t(item.skuInfo)
          } : {}, {
            e: common_vendor.t(item.price),
            f: common_vendor.t(item.quantity),
            g: common_vendor.t((item.price * item.quantity).toFixed(2)),
            h: item.id
          });
        }),
        g: remark.value,
        h: common_vendor.o(($event) => remark.value = $event.detail.value, "d9"),
        i: common_vendor.t(totalPrice.value),
        j: common_vendor.o(doSubmit, "ea")
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-fd186f5c"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/checkout/checkout.js.map

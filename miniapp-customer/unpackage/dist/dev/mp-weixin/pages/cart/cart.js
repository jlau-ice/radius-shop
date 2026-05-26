"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const store_cart = require("../../store/cart.js");
const _sfc_main = {
  __name: "cart",
  setup(__props) {
    const cartStore = store_cart.useCartStore();
    const items = common_vendor.computed(() => cartStore.items);
    const allChecked = common_vendor.computed(() => items.value.length > 0 && items.value.every((i) => i.checked));
    const checkedCount = common_vendor.computed(() => items.value.filter((i) => i.checked).length);
    const totalPrice = common_vendor.computed(() => {
      return items.value.filter((i) => i.checked).reduce((s, i) => s + i.price * i.quantity, 0).toFixed(2);
    });
    common_vendor.onMounted(() => cartStore.fetch());
    async function toggleCheck(item) {
      await cartStore.update(item.id, item.quantity, item.checked ? 0 : 1);
    }
    async function toggleAll() {
      const newChecked = allChecked.value ? 0 : 1;
      for (const item of items.value) {
        if (item.checked !== newChecked)
          await cartStore.update(item.id, item.quantity, newChecked);
      }
    }
    async function changeQty(item, diff) {
      const qty = item.quantity + diff;
      if (qty < 1)
        return;
      await cartStore.update(item.id, qty, item.checked);
    }
    function goCheckout() {
      if (checkedCount.value === 0) {
        common_vendor.index.showToast({ title: "请选择商品", icon: "none" });
        return;
      }
      common_vendor.index.navigateTo({ url: "/pages/checkout/checkout" });
    }
    function goHome() {
      common_vendor.index.switchTab({ url: "/pages/index/index" });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: items.value.length > 0
      }, items.value.length > 0 ? common_vendor.e({
        b: common_vendor.f(items.value, (item, k0, i0) => {
          return common_vendor.e({
            a: item.checked
          }, item.checked ? {} : {}, {
            b: item.checked ? 1 : "",
            c: common_vendor.o(($event) => toggleCheck(item), item.id),
            d: item.coverImage,
            e: common_vendor.t(item.productName),
            f: item.skuInfo
          }, item.skuInfo ? {
            g: common_vendor.t(item.skuInfo)
          } : {}, {
            h: common_vendor.t(item.price),
            i: common_vendor.o(($event) => changeQty(item, -1), item.id),
            j: common_vendor.t(item.quantity),
            k: common_vendor.o(($event) => changeQty(item, 1), item.id),
            l: item.id
          });
        }),
        c: allChecked.value
      }, allChecked.value ? {} : {}, {
        d: allChecked.value ? 1 : "",
        e: common_vendor.o(toggleAll, "1c"),
        f: common_vendor.t(totalPrice.value),
        g: common_vendor.t(checkedCount.value),
        h: common_vendor.o(goCheckout, "bd")
      }) : {
        i: common_assets._imports_0,
        j: common_vendor.o(goHome, "aa")
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-c91e7611"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/cart/cart.js.map

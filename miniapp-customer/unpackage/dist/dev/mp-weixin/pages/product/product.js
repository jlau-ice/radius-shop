"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const store_cart = require("../../store/cart.js");
const _sfc_main = {
  __name: "product",
  setup(__props) {
    const cartStore = store_cart.useCartStore();
    const product = common_vendor.ref(null);
    const selectedSku = common_vendor.ref(0);
    const images = common_vendor.computed(() => {
      var _a;
      return ((_a = product.value) == null ? void 0 : _a.images) || [];
    });
    const cartCount = common_vendor.computed(() => cartStore.totalCount);
    common_vendor.onMounted(async () => {
      var _a;
      const pages = getCurrentPages();
      const query = ((_a = pages[pages.length - 1].$page) == null ? void 0 : _a.options) || {};
      try {
        product.value = await api_index.getProductDetail(query.id);
      } catch {
      }
    });
    async function addCart() {
      await cartStore.add(product.value.id, String(selectedSku.value), 1);
      common_vendor.index.showToast({ title: "已加入购物袋", icon: "success" });
    }
    async function buyNow() {
      await addCart();
      common_vendor.index.switchTab({ url: "/pages/cart/cart" });
    }
    function goCart() {
      common_vendor.index.switchTab({ url: "/pages/cart/cart" });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: product.value
      }, product.value ? common_vendor.e({
        b: common_vendor.f(images.value, (img, i, i0) => {
          return {
            a: img,
            b: i
          };
        }),
        c: images.value.length === 0
      }, images.value.length === 0 ? {
        d: product.value.coverImage
      } : {}, {
        e: common_vendor.t(product.value.name),
        f: common_vendor.t(product.value.sales || 0),
        g: product.value.description
      }, product.value.description ? {
        h: common_vendor.t(product.value.description)
      } : {}, {
        i: common_vendor.t(product.value.price),
        j: product.value.originPrice
      }, product.value.originPrice ? {
        k: common_vendor.t(product.value.originPrice)
      } : {}, {
        l: product.value.skuList && product.value.skuList.length > 0
      }, product.value.skuList && product.value.skuList.length > 0 ? {
        m: common_vendor.f(product.value.skuList, (sku, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(sku.specValue),
            b: sku.priceDiff > 0
          }, sku.priceDiff > 0 ? {
            c: common_vendor.t(sku.priceDiff)
          } : {}, {
            d: selectedSku.value === sku.id ? 1 : "",
            e: sku.id,
            f: common_vendor.o(($event) => selectedSku.value = sku.id, sku.id)
          });
        })
      } : {}, {
        n: cartCount.value
      }, cartCount.value ? {
        o: common_vendor.t(cartCount.value)
      } : {}, {
        p: common_vendor.o(goCart, "7e"),
        q: common_vendor.o(addCart, "62"),
        r: common_vendor.o(buyNow, "66")
      }) : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-946a9793"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/product/product.js.map

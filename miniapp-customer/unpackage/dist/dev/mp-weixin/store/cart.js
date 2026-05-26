"use strict";
const common_vendor = require("../common/vendor.js");
const api_index = require("../api/index.js");
const useCartStore = common_vendor.defineStore("cart", {
  state: () => ({
    items: [],
    totalCount: 0
  }),
  actions: {
    async fetch() {
      try {
        const list = await api_index.getCartList();
        this.items = list || [];
        this.totalCount = this.items.filter((i) => i.checked).reduce((s, i) => s + i.quantity, 0);
      } catch {
      }
    },
    async add(productId, skuInfo, quantity) {
      await api_index.addToCart({ productId, skuInfo, quantity });
      await this.fetch();
    },
    async update(id, quantity, checked) {
      await api_index.updateCartItem({ id, quantity, checked });
      await this.fetch();
    },
    async remove(ids) {
      await api_index.removeCartItems(ids);
      await this.fetch();
    }
  }
});
exports.useCartStore = useCartStore;
//# sourceMappingURL=../../.sourcemap/mp-weixin/store/cart.js.map

"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/index/index.js";
  "./pages/category/category.js";
  "./pages/product/product.js";
  "./pages/cart/cart.js";
  "./pages/checkout/checkout.js";
  "./pages/address/address.js";
  "./pages/order/order.js";
  "./pages/order-detail/order-detail.js";
  "./pages/mine/mine.js";
}
const _sfc_main = {
  onLaunch() {
    common_vendor.index.__f__("log", "at App.vue:4", "App launched");
  }
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  app.use(common_vendor.createPinia());
  return { app };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map

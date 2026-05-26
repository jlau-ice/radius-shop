"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/login/login.js";
  "./pages/index/index.js";
  "./pages/order-detail/order-detail.js";
  "./pages/complete/complete.js";
  "./pages/records/records.js";
  "./pages/mine/mine.js";
}
const _sfc_main = {
  onLaunch() {
    const token = common_vendor.index.getStorageSync("token");
    if (!token) {
      common_vendor.index.reLaunch({ url: "/pages/login/login" });
    }
  }
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  return { app };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map

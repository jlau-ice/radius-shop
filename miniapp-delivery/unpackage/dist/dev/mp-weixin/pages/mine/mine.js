"use strict";
const common_vendor = require("../../common/vendor.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  __name: "mine",
  setup(__props) {
    function goDelivering() {
      common_vendor.index.switchTab({ url: "/pages/index/index" });
    }
    function doLogout() {
      common_vendor.index.removeStorageSync("token");
      common_vendor.index.reLaunch({ url: "/pages/login/login" });
    }
    return (_ctx, _cache) => {
      return {
        a: common_assets._imports_0,
        b: common_vendor.o(goDelivering, "94"),
        c: common_vendor.o(doLogout, "7b")
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-7c2ebfa5"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/mine/mine.js.map

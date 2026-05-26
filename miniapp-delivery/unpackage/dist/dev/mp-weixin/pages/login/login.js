"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "login",
  setup(__props) {
    const username = common_vendor.ref("");
    const password = common_vendor.ref("");
    const loading = common_vendor.ref(false);
    async function doLogin() {
      if (!username.value || !password.value) {
        common_vendor.index.showToast({ title: "请输入账号密码", icon: "none" });
        return;
      }
      loading.value = true;
      try {
        const data = await api_index.login(username.value, password.value);
        common_vendor.index.setStorageSync("token", data.token);
        common_vendor.index.reLaunch({ url: "/pages/index/index" });
      } catch {
      } finally {
        loading.value = false;
      }
    }
    return (_ctx, _cache) => {
      return {
        a: username.value,
        b: common_vendor.o(($event) => username.value = $event.detail.value, "c6"),
        c: password.value,
        d: common_vendor.o(($event) => password.value = $event.detail.value, "26"),
        e: loading.value,
        f: common_vendor.o(doLogin, "80")
      };
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-e4e4508d"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/login/login.js.map

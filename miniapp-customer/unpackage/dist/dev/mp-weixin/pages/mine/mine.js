"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "mine",
  setup(__props) {
    const user = common_vendor.ref({});
    common_vendor.onMounted(async () => {
      const token = common_vendor.index.getStorageSync("token");
      if (token) {
        try {
          user.value = await api_index.getUserInfo();
        } catch {
        }
      }
    });
    async function doLogin() {
      if (user.value.id)
        return;
      common_vendor.index.login({
        provider: "weixin",
        success: async (res) => {
          try {
            const data = await api_index.wxLogin(res.code);
            common_vendor.index.setStorageSync("token", data.token);
            user.value = data.user;
          } catch {
          }
        }
      });
    }
    function goOrders(status) {
      common_vendor.index.setStorageSync("orderStatusFilter", status);
      common_vendor.index.switchTab({ url: "/pages/order/order" });
    }
    function callService() {
      common_vendor.index.showToast({ title: "客服微信: xxxxx", icon: "none", duration: 3e3 });
    }
    async function doLogout() {
      try {
        await api_index.logout();
      } catch {
      }
      common_vendor.index.removeStorageSync("token");
      user.value = {};
      common_vendor.index.showToast({ title: "已退出", icon: "none" });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: user.value.avatarUrl || "/static/default-avatar.png",
        b: common_vendor.t(user.value.nickname || "点击登录"),
        c: common_vendor.t(user.value.phone || "登录后查看订单与配送进度"),
        d: common_vendor.o(doLogin, "13"),
        e: common_vendor.o(($event) => goOrders("pending"), "27"),
        f: common_vendor.o(($event) => goOrders("delivering"), "8a"),
        g: common_vendor.o(($event) => goOrders(""), "1b"),
        h: common_vendor.o(($event) => goOrders(""), "fd"),
        i: common_vendor.o(callService, "9f"),
        j: user.value.id
      }, user.value.id ? {
        k: common_vendor.o(doLogout, "2a")
      } : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-7c2ebfa5"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/mine/mine.js.map

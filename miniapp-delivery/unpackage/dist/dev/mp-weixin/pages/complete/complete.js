"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "complete",
  setup(__props) {
    const photoPath = common_vendor.ref("");
    const submitting = common_vendor.ref(false);
    function takePhoto() {
      common_vendor.index.chooseImage({
        count: 1,
        sourceType: ["camera"],
        success(res) {
          photoPath.value = res.tempFilePaths[0];
        }
      });
    }
    async function doSubmit() {
      var _a, _b;
      submitting.value = true;
      try {
        const pages = getCurrentPages();
        const id = (_b = (_a = pages[pages.length - 1].$page) == null ? void 0 : _a.options) == null ? void 0 : _b.id;
        await api_index.completeOrder(id, photoPath.value);
        common_vendor.index.showToast({ title: "已签收" });
        setTimeout(() => common_vendor.index.switchTab({ url: "/pages/index/index" }), 1e3);
      } catch {
      } finally {
        submitting.value = false;
      }
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: photoPath.value
      }, photoPath.value ? {
        b: photoPath.value
      } : {}, {
        c: common_vendor.o(takePhoto, "33"),
        d: submitting.value,
        e: !photoPath.value,
        f: common_vendor.o(doSubmit, "c4")
      });
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-9b6f5ba0"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/complete/complete.js.map

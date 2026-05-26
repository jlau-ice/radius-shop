"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "address",
  setup(__props) {
    const list = common_vendor.ref([]);
    const selectedId = common_vendor.ref(null);
    common_vendor.onMounted(async () => {
      try {
        list.value = await api_index.getAddressList();
      } catch {
      }
    });
    function select(addr) {
      selectedId.value = addr.id;
      const pages = getCurrentPages();
      const prev = pages[pages.length - 2];
      if (prev)
        prev.__exposeAddr = addr;
      common_vendor.index.navigateBack();
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.f(list.value, (addr, k0, i0) => {
          return common_vendor.e({
            a: common_vendor.t(addr.name),
            b: common_vendor.t(addr.address),
            c: addr.contactName
          }, addr.contactName ? {
            d: common_vendor.t(addr.contactName),
            e: common_vendor.t(addr.contactPhone)
          } : {}, {
            f: selectedId.value === addr.id
          }, selectedId.value === addr.id ? {} : {}, {
            g: addr.id,
            h: common_vendor.o(($event) => select(addr), addr.id)
          });
        }),
        b: list.value.length === 0
      }, list.value.length === 0 ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-40ca010a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/address/address.js.map

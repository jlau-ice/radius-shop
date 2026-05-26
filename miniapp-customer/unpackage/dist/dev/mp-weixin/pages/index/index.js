"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "index",
  setup(__props) {
    const categories = common_vendor.ref([]);
    const products = common_vendor.ref([]);
    const activeCid = common_vendor.ref(0);
    const page = common_vendor.ref(1);
    const loading = common_vendor.ref(false);
    const noMore = common_vendor.ref(false);
    const keyword = common_vendor.ref("");
    common_vendor.onMounted(() => {
      loadCategories();
      loadProducts();
    });
    async function loadCategories() {
      try {
        categories.value = await api_index.getCategories();
      } catch {
      }
    }
    async function loadProducts(reset = false) {
      if (loading.value)
        return;
      loading.value = true;
      if (reset) {
        page.value = 1;
        products.value = [];
      }
      try {
        const data = await api_index.getProductPage({ page: page.value, pageSize: 10, categoryId: activeCid.value || void 0, keyword: keyword.value || void 0 });
        const records = data.records || [];
        if (reset)
          products.value = records;
        else
          products.value = [...products.value, ...records];
        noMore.value = records.length < 10;
        page.value++;
      } catch {
      } finally {
        loading.value = false;
      }
    }
    function switchCategory(e) {
      activeCid.value = Number(e.currentTarget.dataset.id);
      loadProducts(true);
    }
    function doSearch(e) {
      keyword.value = e.detail.value;
      loadProducts(true);
    }
    function goDetail(id) {
      common_vendor.index.navigateTo({ url: `/pages/product/product?id=${id}` });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: common_vendor.o(doSearch, "e5"),
        b: activeCid.value === 0 ? 1 : "",
        c: common_vendor.o(switchCategory, "29"),
        d: common_vendor.f(categories.value, (c, k0, i0) => {
          return {
            a: common_vendor.t(c.name),
            b: activeCid.value === c.id ? 1 : "",
            c: c.id,
            d: c.id,
            e: common_vendor.o(switchCategory, c.id)
          };
        }),
        e: common_vendor.f(products.value, (p, k0, i0) => {
          return common_vendor.e({
            a: p.coverImage,
            b: common_vendor.t(p.name),
            c: common_vendor.t(p.sales || 0),
            d: common_vendor.t(p.price),
            e: p.originPrice
          }, p.originPrice ? {
            f: common_vendor.t(p.originPrice)
          } : {}, {
            g: p.id,
            h: common_vendor.o(($event) => goDetail(p.id), p.id)
          });
        }),
        f: loading.value
      }, loading.value ? {} : {}, {
        g: noMore.value
      }, noMore.value ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-1cf27b2a"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map

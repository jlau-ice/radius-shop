"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  __name: "category",
  setup(__props) {
    const categories = common_vendor.ref([]);
    const products = common_vendor.ref([]);
    const activeId = common_vendor.ref(0);
    const loading = common_vendor.ref(false);
    common_vendor.onMounted(async () => {
      try {
        categories.value = await api_index.getCategories();
        if (categories.value.length > 0) {
          activeId.value = categories.value[0].id;
          loadProducts();
        }
      } catch {
      }
    });
    async function loadProducts() {
      loading.value = true;
      try {
        const data = await api_index.getProductPage({ categoryId: activeId.value, pageSize: 50 });
        products.value = data.records || [];
      } catch {
      } finally {
        loading.value = false;
      }
    }
    function goDetail(id) {
      common_vendor.index.navigateTo({ url: `/pages/product/product?id=${id}` });
    }
    return (_ctx, _cache) => {
      return common_vendor.e({
        a: categories.value.length > 0
      }, categories.value.length > 0 ? {
        b: common_vendor.f(categories.value, (c, k0, i0) => {
          return {
            a: common_vendor.t(c.name),
            b: activeId.value === c.id ? 1 : "",
            c: c.id,
            d: common_vendor.o(($event) => {
              activeId.value = c.id;
              loadProducts();
            }, c.id)
          };
        })
      } : {}, {
        c: common_vendor.f(products.value, (p, k0, i0) => {
          return {
            a: p.coverImage,
            b: common_vendor.t(p.name),
            c: common_vendor.t(p.price),
            d: p.id,
            e: common_vendor.o(($event) => goDetail(p.id), p.id)
          };
        }),
        d: products.value.length === 0 && !loading.value
      }, products.value.length === 0 && !loading.value ? {} : {});
    };
  }
};
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["__scopeId", "data-v-8145b772"]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/category/category.js.map

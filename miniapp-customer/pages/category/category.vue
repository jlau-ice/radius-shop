<template>
  <view class="category-page">
    <scroll-view scroll-y class="left-nav" v-if="categories.length > 0">
      <view class="nav-item" :class="{ active: activeId === c.id }" v-for="c in categories" :key="c.id" @click="activeId = c.id; loadProducts()">
        <text>{{ c.name }}</text>
      </view>
    </scroll-view>
    <scroll-view scroll-y class="right-content">
      <view class="category-title">分类甜品</view>
      <view class="product-card" v-for="p in products" :key="p.id" @click="goDetail(p.id)">
        <image :src="p.coverImage" mode="aspectFill" class="product-img" />
        <view class="product-info">
          <view class="product-name">{{ p.name }}</view>
          <view class="product-desc">手作新鲜 · 今日供应</view>
          <view class="product-price">¥{{ p.price }}</view>
        </view>
        <view class="add-mark">＋</view>
      </view>
      <view class="empty" v-if="products.length === 0 && !loading">这一类还在准备中</view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCategories, getProductPage } from '@/api/index'

const categories = ref([])
const products = ref([])
const activeId = ref(0)
const loading = ref(false)

onMounted(async () => {
  try {
    categories.value = await getCategories()
    if (categories.value.length > 0) {
      activeId.value = categories.value[0].id
      loadProducts()
    }
  } catch { /* */ }
})

async function loadProducts() {
  loading.value = true
  try {
    const data = await getProductPage({ categoryId: activeId.value, pageSize: 50 })
    products.value = data.records || []
  } catch { /* */ }
  finally { loading.value = false }
}

function goDetail(id) {
  uni.navigateTo({ url: `/pages/product/product?id=${id}` })
}
</script>

<style scoped>
.category-page { display: flex; height: calc(100vh - 50px); background: #f6f1e8; }
.left-nav { width: 96px; padding-top: 12px; background: linear-gradient(180deg, #efe3cf, #f6f1e8); }
.nav-item { margin: 0 10px 10px 12px; padding: 13px 8px; border-radius: 16px; font-size: 13px; text-align: center; color: #746754; }
.nav-item.active { background: #1f5a46; color: #fbefd8; font-weight: 700; box-shadow: 0 10px 22px rgba(31,90,70,.22); }
.right-content { flex: 1; padding: 14px 12px 24px; box-sizing: border-box; }
.category-title { margin: 2px 0 12px; color: #24392f; font-size: 21px; font-weight: 800; }
.product-card { position: relative; display: flex; padding: 10px; margin-bottom: 12px; border-radius: 22px; background: #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(79, 55, 30, .08); }
.product-img { width: 96px; height: 96px; border-radius: 18px; background: #eee1cf; }
.product-info { flex: 1; min-width: 0; margin-left: 12px; padding-top: 4px; }
.product-name { font-size: 15px; font-weight: 800; color: #22352d; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.product-desc { margin-top: 7px; font-size: 11px; color: #968978; }
.product-price { margin-top: 15px; color: #1f5a46; font-size: 18px; font-weight: 800; }
.add-mark { position: absolute; right: 12px; bottom: 12px; width: 26px; height: 26px; line-height: 26px; border-radius: 13px; text-align: center; color: #fff5df; background: #1f5a46; box-shadow: 0 8px 14px rgba(31,90,70,.2); }
.empty { text-align: center; color: #9a8d78; padding-top: 80px; font-size: 13px; }
</style>

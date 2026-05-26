<template>
  <view class="home">
    <view class="hero">
      <view class="hero-mark">甜品小屋</view>
      <view class="hero-title">今日手作，温柔上新</view>
      <view class="hero-subtitle">新中式甜品 · 鲜果小食 · 下午茶灵感</view>
      <view class="hero-card">
        <view>
          <view class="hero-card-title">本周推荐</view>
          <view class="hero-card-text">招牌甜品与清爽饮品搭配</view>
        </view>
        <view class="hero-badge">新品</view>
      </view>
    </view>

    <view class="search-wrap">
      <view class="search-bar">
        <text class="search-icon">⌕</text>
        <input class="search-input" placeholder="搜索芋泥、雪媚娘、奶茶..." placeholder-class="placeholder" @confirm="doSearch" />
      </view>
    </view>

    <scroll-view scroll-x class="category-scroll" show-scrollbar="false">
      <view class="category-item" :class="{ active: activeCid === 0 }" :data-id="0" @click="switchCategory">全部</view>
      <view class="category-item" :class="{ active: activeCid === c.id }" v-for="c in categories" :key="c.id" :data-id="c.id" @click="switchCategory">{{ c.name }}</view>
    </scroll-view>

    <view class="section-head">
      <view>
        <view class="section-kicker">MENU</view>
        <view class="section-title">甜品精选</view>
      </view>
      <text class="section-note">现点现做</text>
    </view>

    <view class="product-grid">
      <view class="product-card" v-for="p in products" :key="p.id" @click="goDetail(p.id)">
        <image :src="p.coverImage" mode="aspectFill" class="product-img" />
        <view class="product-info">
          <view class="product-name">{{ p.name }}</view>
          <view class="product-sales">已售 {{ p.sales || 0 }} · 手作甜品</view>
          <view class="product-bottom">
            <view class="product-price"><text class="price-symbol">¥</text>{{ p.price }}</view>
            <view class="add-dot">＋</view>
          </view>
          <view class="origin-price" v-if="p.originPrice">¥{{ p.originPrice }}</view>
        </view>
      </view>
    </view>

    <view class="loading-text" v-if="loading">正在准备菜单...</view>
    <view class="loading-text" v-if="noMore">— 今日甜品暂时这些 —</view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getCategories, getProductPage } from '@/api/index'

const categories = ref([])
const products = ref([])
const activeCid = ref(0)
const page = ref(1)
const loading = ref(false)
const noMore = ref(false)
const keyword = ref('')

onMounted(() => { loadCategories(); loadProducts() })

async function loadCategories() {
  try { categories.value = await getCategories() } catch { /* */ }
}

async function loadProducts(reset = false) {
  if (loading.value) return
  loading.value = true
  if (reset) { page.value = 1; products.value = [] }
  try {
    const data = await getProductPage({ page: page.value, pageSize: 10, categoryId: activeCid.value || undefined, keyword: keyword.value || undefined })
    const records = data.records || []
    if (reset) products.value = records
    else products.value = [...products.value, ...records]
    noMore.value = records.length < 10
    page.value++
  } catch { /* */ }
  finally { loading.value = false }
}

function switchCategory(e) {
  activeCid.value = Number(e.currentTarget.dataset.id)
  loadProducts(true)
}

function doSearch(e) {
  keyword.value = e.detail.value
  loadProducts(true)
}

function goDetail(id) {
  uni.navigateTo({ url: `/pages/product/product?id=${id}` })
}
</script>

<style scoped>
.home { min-height: 100vh; padding-bottom: 30px; background: linear-gradient(180deg, #f4ead9 0%, #f8f3eb 42%, #f6f1e8 100%); }
.hero { margin: 0 16px; padding: 26px 22px 18px; border-radius: 0 0 28px 28px; color: #f8f0df; background: radial-gradient(circle at 85% 8%, rgba(214,178,104,.45), transparent 30%), linear-gradient(145deg, #1d493b, #17362e 68%, #102720); box-shadow: 0 18px 38px rgba(28, 55, 46, .22); position: relative; overflow: hidden; }
.hero::after { content: ''; position: absolute; right: -36px; bottom: -58px; width: 150px; height: 150px; border: 1px solid rgba(229, 202, 146, .28); border-radius: 75px; }
.hero-mark { display: inline-block; padding: 4px 10px; border: 1px solid rgba(229,202,146,.55); border-radius: 999px; color: #e4c986; font-size: 11px; letter-spacing: 3px; }
.hero-title { margin-top: 18px; font-size: 27px; line-height: 1.25; font-weight: 700; letter-spacing: 1px; }
.hero-subtitle { margin-top: 8px; font-size: 13px; color: rgba(248,240,223,.78); }
.hero-card { margin-top: 22px; padding: 12px 14px; border-radius: 18px; background: rgba(255,255,255,.1); border: 1px solid rgba(255,255,255,.16); display: flex; justify-content: space-between; align-items: center; backdrop-filter: blur(10px); }
.hero-card-title { font-size: 14px; color: #fff5df; font-weight: 600; }
.hero-card-text { margin-top: 3px; font-size: 11px; color: rgba(248,240,223,.68); }
.hero-badge { padding: 6px 10px; border-radius: 999px; color: #1d493b; background: #e2c078; font-size: 12px; font-weight: 700; }
.search-wrap { padding: 16px; }
.search-bar { height: 44px; padding: 0 16px; border-radius: 22px; background: rgba(255,255,255,.92); border: 1px solid #eadfcf; box-shadow: 0 10px 24px rgba(88, 61, 32, .08); display: flex; align-items: center; }
.search-icon { color: #b79052; font-size: 18px; margin-right: 8px; }
.search-input { flex: 1; font-size: 14px; color: #22352d; }
.placeholder { color: #a69a86; }
.category-scroll { white-space: nowrap; padding: 0 16px 14px; box-sizing: border-box; }
.category-item { display: inline-block; padding: 8px 17px; margin-right: 10px; border-radius: 18px; font-size: 13px; background: #fffaf3; color: #786b5a; border: 1px solid #ecdfcc; }
.category-item.active { color: #f9efd9; background: #1f5a46; border-color: #1f5a46; box-shadow: 0 8px 18px rgba(31, 90, 70, .22); }
.section-head { padding: 4px 18px 12px; display: flex; align-items: flex-end; justify-content: space-between; }
.section-kicker { color: #b78e4b; font-size: 10px; letter-spacing: 3px; }
.section-title { color: #24392f; font-size: 22px; font-weight: 800; margin-top: 2px; }
.section-note { font-size: 12px; color: #917f68; }
.product-grid { display: flex; flex-wrap: wrap; padding: 0 10px; }
.product-card { width: calc(50% - 12px); margin: 6px; background: #fffaf2; border-radius: 22px; overflow: hidden; box-shadow: 0 12px 26px rgba(77, 53, 28, .09); border: 1px solid rgba(226, 209, 181, .72); }
.product-img { width: 100%; height: 166px; background: #eee1cf; }
.product-info { padding: 11px 12px 13px; }
.product-name { font-size: 15px; font-weight: 700; color: #22352d; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.product-sales { margin-top: 5px; font-size: 11px; color: #958975; }
.product-bottom { display: flex; align-items: center; justify-content: space-between; margin-top: 8px; }
.product-price { color: #1f5a46; font-size: 19px; font-weight: 800; }
.price-symbol { font-size: 12px; margin-right: 1px; color: #b78e4b; }
.add-dot { width: 28px; height: 28px; line-height: 28px; text-align: center; border-radius: 14px; color: #fff5df; background: linear-gradient(135deg, #23634c, #183b32); font-size: 17px; box-shadow: 0 8px 14px rgba(31,90,70,.22); }
.origin-price { margin-top: 2px; font-size: 11px; color: #b5aa97; text-decoration: line-through; }
.loading-text { text-align: center; color: #978a76; padding: 18px; font-size: 13px; }
</style>

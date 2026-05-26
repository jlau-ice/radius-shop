<template>
  <view class="product-detail" v-if="product">
    <view class="gallery-card">
      <swiper class="swiper" indicator-dots indicator-color="rgba(255,255,255,.45)" indicator-active-color="#e2c078">
        <swiper-item v-for="(img, i) in images" :key="i">
          <image :src="img" mode="aspectFill" class="swiper-img" />
        </swiper-item>
        <swiper-item v-if="images.length === 0">
          <image :src="product.coverImage" mode="aspectFill" class="swiper-img" />
        </swiper-item>
      </swiper>
      <view class="gallery-label">HANDMADE</view>
    </view>

    <view class="info-section">
      <view class="name-row">
        <view class="name">{{ product.name }}</view>
        <view class="sales">已售 {{ product.sales || 0 }}</view>
      </view>
      <view class="desc" v-if="product.description">{{ product.description }}</view>
      <view class="price-row">
        <text class="price-symbol">¥</text><text class="price">{{ product.price }}</text>
        <text class="origin-price" v-if="product.originPrice">¥{{ product.originPrice }}</text>
      </view>
    </view>

    <view class="sku-section" v-if="product.skuList && product.skuList.length > 0">
      <view class="section-title">选择规格</view>
      <view class="sku-list">
        <view class="sku-item" :class="{ active: selectedSku === sku.id }" v-for="sku in product.skuList" :key="sku.id" @click="selectedSku = sku.id">
          {{ sku.specValue }} <text v-if="sku.priceDiff > 0">+¥{{ sku.priceDiff }}</text>
        </view>
      </view>
    </view>

    <view class="bottom-bar">
      <view class="cart-icon" @click="goCart">
        <text>袋</text><view class="badge" v-if="cartCount">{{ cartCount }}</view>
      </view>
      <button class="btn-cart" @click="addCart">加入购物袋</button>
      <button class="btn-buy" @click="buyNow">立即下单</button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getProductDetail } from '@/api/index'
import { useCartStore } from '@/store/cart'

const cartStore = useCartStore()
const product = ref(null)
const selectedSku = ref(0)

const images = computed(() => product.value?.images || [])
const cartCount = computed(() => cartStore.totalCount)

onMounted(async () => {
  const pages = getCurrentPages()
  const query = pages[pages.length - 1].$page?.options || {}
  try { product.value = await getProductDetail(query.id) } catch { /* */ }
})

async function addCart() {
  await cartStore.add(product.value.id, String(selectedSku.value), 1)
  uni.showToast({ title: '已加入购物袋', icon: 'success' })
}

async function buyNow() {
  await addCart()
  uni.switchTab({ url: '/pages/cart/cart' })
}

function goCart() {
  uni.switchTab({ url: '/pages/cart/cart' })
}
</script>

<style scoped>
.product-detail { min-height: 100vh; padding-bottom: 86px; background: #f6f1e8; }
.gallery-card { position: relative; margin: 0 14px; padding-top: 12px; }
.swiper { height: 370px; border-radius: 0 0 32px 32px; overflow: hidden; background: #eee1cf; box-shadow: 0 18px 34px rgba(68, 45, 23, .16); }
.swiper-img { width: 100%; height: 100%; }
.gallery-label { position: absolute; left: 18px; bottom: 18px; padding: 6px 12px; border-radius: 999px; color: #1f5a46; background: rgba(251,247,239,.88); font-size: 10px; letter-spacing: 2px; font-weight: 800; }
.info-section { margin: 14px 16px 0; padding: 18px; border-radius: 24px; background: #fffaf2; border: 1px solid #eadbc4; box-shadow: 0 12px 24px rgba(83, 59, 33, .08); }
.name-row { display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; }
.name { flex: 1; color: #22352d; font-size: 22px; line-height: 1.3; font-weight: 900; }
.sales { padding: 5px 9px; border-radius: 12px; background: #f0e4d0; color: #8a7558; font-size: 11px; white-space: nowrap; }
.desc { margin-top: 10px; font-size: 13px; color: #746856; line-height: 1.8; }
.price-row { margin-top: 14px; display: flex; align-items: baseline; }
.price-symbol { color: #b78e4b; font-size: 14px; font-weight: 800; }
.price { color: #1f5a46; font-size: 28px; font-weight: 900; }
.origin-price { font-size: 13px; color: #aa9f8d; text-decoration: line-through; margin-left: 9px; }
.sku-section { margin: 12px 16px 0; padding: 16px; border-radius: 22px; background: #fffaf2; border: 1px solid #eadbc4; }
.section-title { font-size: 15px; color: #22352d; font-weight: 800; margin-bottom: 12px; }
.sku-list { display: flex; flex-wrap: wrap; gap: 9px; }
.sku-item { padding: 8px 16px; border-radius: 18px; font-size: 13px; background: #f4ead9; color: #6f624f; border: 1px solid #eadbc4; }
.sku-item.active { background: #1f5a46; color: #fff2d8; border-color: #1f5a46; box-shadow: 0 8px 16px rgba(31,90,70,.18); }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; background: rgba(251,247,239,.96); display: flex; align-items: center; padding: 10px 14px 18px; border-top: 1px solid #eadbc4; box-shadow: 0 -12px 26px rgba(44, 32, 18, .08); }
.cart-icon { width: 46px; height: 46px; border-radius: 23px; background: #efe2ce; color: #1f5a46; display: flex; align-items: center; justify-content: center; font-size: 14px; font-weight: 800; margin-right: 10px; position: relative; }
.badge { position: absolute; top: -5px; right: -4px; min-width: 17px; height: 17px; line-height: 17px; text-align: center; background: #bd8b3b; color: #fff; font-size: 10px; border-radius: 9px; }
.btn-cart { flex: 1; height: 44px; line-height: 44px; color: #1f5a46; background: #fffaf2; border: 1px solid #1f5a46; border-radius: 22px; font-size: 14px; margin-right: 10px; }
.btn-buy { flex: 1; height: 44px; line-height: 44px; color: #fff2d8; background: linear-gradient(135deg, #23634c, #153c31); border-radius: 22px; font-size: 14px; box-shadow: 0 10px 20px rgba(31,90,70,.22); }
</style>

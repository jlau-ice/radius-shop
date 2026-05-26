<template>
  <view class="cart-page">
    <block v-if="items.length > 0">
      <view class="cart-title">购物袋</view>
      <view class="cart-subtitle">为今天留一点甜</view>

      <view class="cart-item" v-for="item in items" :key="item.id">
        <view class="checkbox" :class="{ checkedBox: item.checked }" @click="toggleCheck(item)">
          <text v-if="item.checked">✓</text>
        </view>
        <image :src="item.coverImage" mode="aspectFill" class="item-img" />
        <view class="item-info">
          <view class="item-name">{{ item.productName }}</view>
          <view class="item-sku" v-if="item.skuInfo">{{ item.skuInfo }}</view>
          <view class="item-price-row">
            <text class="item-price">¥{{ item.price }}</text>
            <view class="qty-ctrl">
              <text class="qty-btn" @click="changeQty(item, -1)">−</text>
              <text class="qty">{{ item.quantity }}</text>
              <text class="qty-btn plus" @click="changeQty(item, 1)">＋</text>
            </view>
          </view>
        </view>
      </view>

      <view class="bottom-bar">
        <view class="total" @click="toggleAll">
          <view class="mini-check" :class="{ checkedBox: allChecked }"><text v-if="allChecked">✓</text></view>
          <text>全选</text>
        </view>
        <view class="total-price">
          <view class="total-label">合计</view>
          <text>¥{{ totalPrice }}</text>
        </view>
        <button class="btn-submit" @click="goCheckout">结算 {{ checkedCount }}</button>
      </view>
    </block>

    <view class="empty" v-else>
      <image src="/static/empty-cart.png" mode="aspectFit" class="empty-img" />
      <text class="empty-title">购物袋还是空的</text>
      <text class="empty-desc">去选一份今日手作甜品吧</text>
      <button class="go-shop" @click="goHome">去逛逛</button>
    </view>
  </view>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useCartStore } from '@/store/cart'

const cartStore = useCartStore()

const items = computed(() => cartStore.items)
const allChecked = computed(() => items.value.length > 0 && items.value.every(i => i.checked))
const checkedCount = computed(() => items.value.filter(i => i.checked).length)
const totalPrice = computed(() => {
  return items.value.filter(i => i.checked).reduce((s, i) => s + i.price * i.quantity, 0).toFixed(2)
})

onMounted(() => cartStore.fetch())

async function toggleCheck(item) {
  await cartStore.update(item.id, item.quantity, item.checked ? 0 : 1)
}

async function toggleAll() {
  const newChecked = allChecked.value ? 0 : 1
  for (const item of items.value) {
    if (item.checked !== newChecked) await cartStore.update(item.id, item.quantity, newChecked)
  }
}

async function changeQty(item, diff) {
  const qty = item.quantity + diff
  if (qty < 1) return
  await cartStore.update(item.id, qty, item.checked)
}

function goCheckout() {
  if (checkedCount.value === 0) {
    uni.showToast({ title: '请选择商品', icon: 'none' })
    return
  }
  uni.navigateTo({ url: '/pages/checkout/checkout' })
}

function goHome() {
  uni.switchTab({ url: '/pages/index/index' })
}
</script>

<style scoped>
.cart-page { min-height: 100vh; padding: 18px 14px 86px; background: #f6f1e8; box-sizing: border-box; }
.cart-title { color: #22352d; font-size: 26px; font-weight: 900; }
.cart-subtitle { margin: 4px 0 16px; color: #9a8b73; font-size: 13px; }
.cart-item { display: flex; padding: 12px; margin-bottom: 12px; border-radius: 24px; background: #fffaf2; border: 1px solid #eadbc4; align-items: center; box-shadow: 0 12px 24px rgba(83, 59, 33, .08); }
.checkbox, .mini-check { width: 22px; height: 22px; line-height: 22px; text-align: center; border-radius: 11px; border: 1px solid #cdbb9d; color: #fff7e8; font-size: 13px; margin-right: 10px; flex-shrink: 0; }
.checkedBox { background: #1f5a46; border-color: #1f5a46; }
.item-img { width: 82px; height: 82px; border-radius: 18px; background: #eee1cf; }
.item-info { flex: 1; min-width: 0; margin-left: 12px; }
.item-name { color: #22352d; font-size: 15px; font-weight: 800; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.item-sku { color: #9b8d77; font-size: 11px; margin-top: 4px; }
.item-price-row { display: flex; justify-content: space-between; align-items: center; margin-top: 12px; }
.item-price { color: #1f5a46; font-weight: 900; font-size: 17px; }
.qty-ctrl { display: flex; align-items: center; background: #f3e8d7; border-radius: 16px; padding: 2px; }
.qty-btn { width: 24px; height: 24px; line-height: 24px; text-align: center; border-radius: 12px; color: #1f5a46; font-size: 15px; }
.qty-btn.plus { background: #1f5a46; color: #fff4dc; }
.qty { min-width: 24px; text-align: center; font-size: 13px; color: #22352d; }
.bottom-bar { position: fixed; bottom: 0; left: 0; right: 0; background: rgba(251,247,239,.98); display: flex; align-items: center; padding: 10px 14px 18px; border-top: 1px solid #eadbc4; box-shadow: 0 -12px 26px rgba(44, 32, 18, .08); }
.total { display: flex; align-items: center; font-size: 13px; color: #6d604e; }
.total .mini-check { margin-right: 6px; }
.total-price { flex: 1; margin-left: 14px; }
.total-label { font-size: 11px; color: #9a8d78; }
.total-price text { color: #1f5a46; font-weight: 900; font-size: 19px; }
.btn-submit { width: 116px; height: 44px; line-height: 44px; background: linear-gradient(135deg, #23634c, #153c31); color: #fff2d8; border-radius: 22px; font-size: 14px; box-shadow: 0 10px 20px rgba(31,90,70,.22); }
.empty { text-align: center; padding-top: 118px; }
.empty-img { width: 150px; height: 150px; opacity: .85; }
.empty-title { display: block; color: #22352d; font-size: 17px; font-weight: 800; margin-top: 14px; }
.empty-desc { display: block; color: #9a8d78; font-size: 13px; margin-top: 5px; }
.go-shop { margin-top: 22px; background: #1f5a46; color: #fff2d8; border-radius: 22px; font-size: 14px; width: 138px; height: 42px; line-height: 42px; }
</style>

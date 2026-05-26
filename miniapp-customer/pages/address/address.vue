<template>
  <view class="address-page">
    <view class="page-title">选择配送地址</view>
    <view class="addr-item" v-for="addr in list" :key="addr.id" @click="select(addr)">
      <view class="addr-name">{{ addr.name }}</view>
      <view class="addr-detail">{{ addr.address }}</view>
      <view class="addr-contact" v-if="addr.contactName">{{ addr.contactName }} {{ addr.contactPhone }}</view>
      <text class="check" v-if="selectedId === addr.id">✓</text>
    </view>
    <view class="empty" v-if="list.length === 0">暂无可用配送地址</view>
  </view>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getAddressList } from '@/api/index'

const list = ref([])
const selectedId = ref(null)

onMounted(async () => {
  try { list.value = await getAddressList() } catch { /* */ }
})

function select(addr) {
  selectedId.value = addr.id
  const pages = getCurrentPages()
  const prev = pages[pages.length - 2]
  if (prev) prev.__exposeAddr = addr
  uni.navigateBack()
}
</script>

<style scoped>
.address-page { min-height: 100vh; padding: 18px 14px 28px; background: #f6f1e8; box-sizing: border-box; }
.page-title { color: #22352d; font-size: 24px; font-weight: 900; margin-bottom: 14px; }
.addr-item { background: #fffaf2; padding: 16px; margin-bottom: 12px; border-radius: 24px; border: 1px solid #eadbc4; position: relative; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.addr-name { color: #22352d; font-size: 16px; font-weight: 900; padding-right: 36px; }
.addr-detail { color: #746856; font-size: 13px; margin-top: 7px; line-height: 1.6; padding-right: 36px; }
.addr-contact { color: #9a8d78; font-size: 12px; margin-top: 6px; }
.check { position: absolute; right: 18px; top: 50%; transform: translateY(-50%); width: 28px; height: 28px; line-height: 28px; border-radius: 14px; text-align: center; color: #fff2d8; background: #1f5a46; font-size: 16px; }
.empty { text-align: center; padding-top: 90px; color: #9a8d78; font-size: 14px; }
</style>

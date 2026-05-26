<template>
  <view class="complete-page">
    <view class="page-title">拍照签收</view>
    <view class="tip">请拍摄清晰的送达照片，用于订单留痕</view>
    <view class="photo-area" @click="takePhoto">
      <image v-if="photoPath" :src="photoPath" mode="aspectFill" class="photo" />
      <view v-else class="placeholder">
        <view class="camera">拍</view>
        <text>点击拍照</text>
      </view>
    </view>
    <button class="btn-submit" :loading="submitting" :disabled="!photoPath" @click="doSubmit">确认送达</button>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { completeOrder } from '@/api/index'

const photoPath = ref('')
const submitting = ref(false)

function takePhoto() {
  uni.chooseImage({
    count: 1,
    sourceType: ['camera'],
    success(res) {
      photoPath.value = res.tempFilePaths[0]
    }
  })
}

async function doSubmit() {
  submitting.value = true
  try {
    const pages = getCurrentPages()
    const id = pages[pages.length - 1].$page?.options?.id
    await completeOrder(id, photoPath.value)
    uni.showToast({ title: '已签收' })
    setTimeout(() => uni.switchTab({ url: '/pages/index/index' }), 1000)
  } catch { /* */ }
  finally { submitting.value = false }
}
</script>

<style scoped>
.complete-page { min-height: 100vh; padding: 18px 14px 28px; background: #f6f1e8; box-sizing: border-box; }
.page-title { color: #22352d; font-size: 25px; font-weight: 900; }
.tip { color: #8a7d69; font-size: 13px; margin: 6px 0 16px; }
.photo-area { width: 100%; height: 330px; background: #fffaf2; border: 1px dashed #cdbb9d; border-radius: 28px; display: flex; align-items: center; justify-content: center; overflow: hidden; box-shadow: 0 12px 24px rgba(83,59,33,.08); }
.photo { width: 100%; height: 100%; }
.placeholder { text-align: center; color: #9a8d78; font-size: 14px; }
.camera { width: 54px; height: 54px; line-height: 54px; margin: 0 auto 10px; border-radius: 27px; background: #1f5a46; color: #fff2d8; font-weight: 900; }
.btn-submit { margin-top: 24px; background: linear-gradient(135deg, #23634c, #153c31); color: #fff2d8; border-radius: 26px; height: 50px; line-height: 50px; font-size: 16px; box-shadow: 0 14px 24px rgba(31,90,70,.24); }
.btn-submit[disabled] { background: #cbbda7; color: #fffaf2; box-shadow: none; }
</style>

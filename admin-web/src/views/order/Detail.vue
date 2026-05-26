<template>
  <div class="page-shell order-detail-page">
    <div class="detail-hero">
      <div>
        <button class="back-link" @click="$router.back()">← 返回订单列表</button>
        <div class="page-kicker">ORDER DETAIL</div>
        <h1>订单详情</h1>
        <p>{{ order.orderNo || '加载中...' }}</p>
      </div>
      <el-tag :type="statusType(order.status)" effect="light" class="status-badge">{{ statusText(order.status) }}</el-tag>
    </div>

    <el-card class="detail-card" v-loading="loading">
      <el-descriptions title="订单信息" :column="2" border>
        <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusType(order.status)" effect="light">{{ statusText(order.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="收货人">{{ order.deliveryContact }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ order.deliveryPhone }}</el-descriptions-item>
        <el-descriptions-item label="配送地址" :span="2">{{ order.deliveryAddress }}</el-descriptions-item>
        <el-descriptions-item label="总金额"><span class="price">¥{{ order.totalAmount }}</span></el-descriptions-item>
        <el-descriptions-item label="实付金额"><span class="price strong">¥{{ order.payAmount }}</span></el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ order.createTime }}</el-descriptions-item>
        <el-descriptions-item label="支付时间">{{ order.payTime || '未支付' }}</el-descriptions-item>
        <el-descriptions-item label="备注" :span="2">{{ order.remark || '无' }}</el-descriptions-item>
      </el-descriptions>

      <div class="section-title">商品明细</div>
      <el-table :data="order.items" size="small">
        <el-table-column label="商品图" width="88">
          <template #default="{ row }">
            <el-image :src="row.productImage" class="item-image" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="productName" label="名称" min-width="140" />
        <el-table-column prop="skuInfo" label="规格" min-width="120" />
        <el-table-column prop="price" label="单价" width="90">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="70" />
        <el-table-column prop="amount" label="小计" width="90">
          <template #default="{ row }"><span class="price">¥{{ row.amount }}</span></template>
        </el-table-column>
      </el-table>

      <div v-if="order.deliveryPhoto" class="delivery-photo">
        <div class="section-title">送达照片</div>
        <el-image :src="order.deliveryPhoto" class="photo" fit="cover" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getOrderDetail } from '@/api/order'

const route = useRoute()
const loading = ref(false)
const order = ref({ items: [] })

const statusMap = { pending: '待支付', paid: '已支付', preparing: '制作中', delivering: '配送中', delivered: '已送达', cancelled: '已取消' }
const typeMap = { pending: 'warning', paid: 'info', preparing: '', delivering: '', delivered: 'success', cancelled: 'danger' }
function statusText(s) { return statusMap[s] || s || '加载中' }
function statusType(s) { return typeMap[s] || 'info' }

onMounted(async () => {
  loading.value = true
  try { order.value = await getOrderDetail(route.params.id) } catch { /* */ }
  finally { loading.value = false }
})
</script>

<style scoped>
.detail-hero { display: flex; align-items: flex-end; justify-content: space-between; gap: 20px; padding: 28px; border-radius: 30px; color: #fff2d8; background: radial-gradient(circle at 85% 20%, rgba(226,192,120,.36), transparent 32%), linear-gradient(135deg, #214f40, #17372e); box-shadow: 0 22px 44px rgba(31,90,70,.18); }
.back-link { margin-bottom: 18px; border: 0; color: #e2c078; background: transparent; font-weight: 900; cursor: pointer; }
.detail-hero h1 { margin: 8px 0 8px; font-size: 34px; font-weight: 900; }
.detail-hero p { color: rgba(255,242,216,.72); }
.status-badge { transform: scale(1.08); }
.detail-card { margin-top: 18px; }
.section-title { margin: 24px 0 12px; color: #17372e; font-size: 18px; font-weight: 900; }
.price { color: #8a5a2b; font-weight: 800; }
.price.strong { color: #1f5a46; font-size: 16px; font-weight: 900; }
.item-image { width: 58px; height: 58px; border-radius: 16px; background: #f6f1e8; border: 1px solid #eadbc4; }
.delivery-photo .photo { max-width: 320px; border-radius: 18px; border: 1px solid #eadbc4; }
@media (max-width: 720px) { .detail-hero { align-items: flex-start; flex-direction: column; } }
</style>

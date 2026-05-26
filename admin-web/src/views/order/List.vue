<template>
  <div class="page-shell">
    <div class="page-heading">
      <div class="page-kicker">ORDERS</div>
      <div class="page-title">订单管理</div>
    </div>

    <div class="toolbar order-toolbar">
      <div>
        <div class="toolbar-title">订单流转</div>
        <div class="toolbar-subtitle">按状态查看支付、制作、配送中的订单</div>
      </div>
      <el-radio-group v-model="search.status" @change="fetchData" class="status-tabs">
        <el-radio-button value="">全部</el-radio-button>
        <el-radio-button value="pending">待支付</el-radio-button>
        <el-radio-button value="paid">已支付</el-radio-button>
        <el-radio-button value="preparing">制作中</el-radio-button>
        <el-radio-button value="delivering">配送中</el-radio-button>
        <el-radio-button value="delivered">已送达</el-radio-button>
        <el-radio-button value="cancelled">已取消</el-radio-button>
      </el-radio-group>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="orderNo" label="订单号" width="190" />
      <el-table-column prop="deliveryContact" label="收货人" width="110" />
      <el-table-column prop="deliveryAddress" label="配送地址" min-width="210" show-overflow-tooltip />
      <el-table-column prop="totalAmount" label="金额" width="100">
        <template #default="{ row }"><span class="price">¥{{ row.payAmount }}</span></template>
      </el-table-column>
      <el-table-column label="状态" width="110">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" effect="light">{{ statusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="170" />
      <el-table-column label="操作" width="210" fixed="right" align="right">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/order/${row.id}`)">详情</el-button>
          <el-button v-if="row.status === 'paid'" size="small" type="success" @click="changeStatus(row, 'preparing')">开始制作</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <el-pagination v-model:current-page="search.page" :page-size="search.pageSize" :total="total"
        layout="total, prev, pager, next" @current-change="fetchData" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getOrderPage, updateOrderStatus } from '@/api/order'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const search = reactive({ page: 1, pageSize: 10, status: '' })

const statusMap = { pending: '待支付', paid: '已支付', preparing: '制作中', delivering: '配送中', delivered: '已送达', cancelled: '已取消' }
const typeMap = { pending: 'warning', paid: 'info', preparing: '', delivering: '', delivered: 'success', cancelled: 'danger' }

function statusText(s) { return statusMap[s] || s }
function statusType(s) { return typeMap[s] || 'info' }

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try {
    const data = await getOrderPage(search)
    tableData.value = data.records
    total.value = data.total
  } catch { /* */ }
  finally { loading.value = false }
}

async function changeStatus(row, status) {
  try {
    await updateOrderStatus(row.id, status)
    row.status = status
    ElMessage.success('状态已更新')
  } catch { /* */ }
}
</script>

<style scoped>
.order-toolbar { align-items: flex-end; }
.toolbar-title { color: #17372e; font-size: 17px; font-weight: 900; }
.toolbar-subtitle { margin-top: 4px; color: #9a8d78; font-size: 13px; }
.status-tabs { display: flex; flex-wrap: wrap; justify-content: flex-end; }
.price { color: #8a5a2b; font-weight: 900; }
@media (max-width: 1100px) { .order-toolbar { align-items: stretch; } .status-tabs { justify-content: flex-start; } }
</style>

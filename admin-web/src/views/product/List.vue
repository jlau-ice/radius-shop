<template>
  <div class="page-shell">
    <div class="page-heading">
      <div class="page-kicker">PRODUCTS</div>
      <div class="page-title">商品管理</div>
    </div>

    <div class="toolbar product-toolbar">
      <div>
        <div class="toolbar-title">商品库</div>
        <div class="toolbar-subtitle">维护小程序展示商品、价格、库存与上下架状态</div>
      </div>
      <div class="toolbar-actions">
        <el-select v-model="search.categoryId" placeholder="分类筛选" clearable class="filter-select">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
        <el-input v-model="search.keyword" placeholder="搜索商品" class="search-input" clearable />
        <el-button type="primary" @click="fetchData">搜索</el-button>
        <el-button type="primary" @click="$router.push('/product/create')">新增商品</el-button>
      </div>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column label="封面" width="92">
        <template #default="{ row }">
          <el-image :src="row.coverImage" class="cover-image" fit="cover" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="名称" min-width="150" />
      <el-table-column prop="categoryName" label="分类" width="120" />
      <el-table-column prop="price" label="售价" width="100">
        <template #default="{ row }"><span class="price">¥{{ row.price }}</span></template>
      </el-table-column>
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column prop="sales" label="销量" width="80" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-switch :model-value="row.status === 1" @change="toggleStatus(row)" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="170" fixed="right" align="right">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/product/${row.id}/edit`)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="doDelete(row.id)">删除</el-button>
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
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductPage, deleteProduct, toggleProductStatus } from '@/api/product'
import { getCategoryList } from '@/api/category'

const loading = ref(false)
const tableData = ref([])
const categories = ref([])
const total = ref(0)
const search = reactive({ page: 1, pageSize: 10, categoryId: null, keyword: '' })

onMounted(() => { fetchData(); loadCategories() })

async function fetchData() {
  loading.value = true
  try {
    const data = await getProductPage(search)
    tableData.value = data.records
    total.value = data.total
  } catch { /* */ }
  finally { loading.value = false }
}

async function loadCategories() {
  try { categories.value = await getCategoryList() } catch { /* */ }
}

async function toggleStatus(row) {
  const newStatus = row.status === 1 ? 0 : 1
  try {
    await toggleProductStatus(row.id, newStatus)
    row.status = newStatus
    ElMessage.success(newStatus === 1 ? '已上架' : '已下架')
  } catch { /* */ }
}

async function doDelete(id) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  try { await deleteProduct(id); ElMessage.success('删除成功'); fetchData() } catch { /* */ }
}
</script>

<style scoped>
.product-toolbar { align-items: flex-end; }
.toolbar-title { color: #17372e; font-size: 17px; font-weight: 900; }
.toolbar-subtitle { margin-top: 4px; color: #9a8d78; font-size: 13px; }
.toolbar-actions { display: flex; align-items: center; gap: 10px; flex-wrap: wrap; justify-content: flex-end; }
.filter-select { width: 150px; }
.search-input { width: 210px; }
.cover-image { width: 58px; height: 58px; border-radius: 16px; background: #f6f1e8; border: 1px solid #eadbc4; }
.price { color: #8a5a2b; font-weight: 900; }
@media (max-width: 900px) { .product-toolbar { align-items: stretch; } .toolbar-actions { justify-content: flex-start; } }
</style>

<template>
  <div class="page-shell">
    <div class="page-heading">
      <div class="page-kicker">DELIVERY AREA</div>
      <div class="page-title">配送区域</div>
    </div>

    <div class="toolbar">
      <div>
        <div class="toolbar-title">配送点位</div>
        <div class="toolbar-subtitle">维护用户下单时可选择的收货点和联系方式</div>
      </div>
      <el-button type="primary" @click="showDialog(null)">新增地址</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="点位名称" width="160" />
      <el-table-column prop="address" label="详细地址" min-width="220" show-overflow-tooltip />
      <el-table-column prop="contactName" label="联系人" width="120" />
      <el-table-column prop="contactPhone" label="电话" width="140" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="light">{{ row.status === 1 ? '启用' : '禁用' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="170" align="right">
        <template #default="{ row }">
          <el-button size="small" @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="doDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="isEdit ? '编辑地址' : '新增地址'" v-model="dialogVisible" width="540px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="84px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="如：阳光小区北门" />
        </el-form-item>
        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="填写完整配送地址" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.contactName" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.contactPhone" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="form.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="doSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getAddressList, saveAddress, updateAddress, deleteAddress } from '@/api/address'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, name: '', address: '', contactName: '', contactPhone: '', sortOrder: 0, status: 1 })
const rules = {
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }],
  address: [{ required: true, message: '请输入地址', trigger: 'blur' }]
}

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try { tableData.value = await getAddressList() } catch { /* */ }
  finally { loading.value = false }
}

function showDialog(row) {
  isEdit.value = !!row
  if (row) Object.assign(form, row)
  else Object.assign(form, { id: null, name: '', address: '', contactName: '', contactPhone: '', sortOrder: 0, status: 1 })
  dialogVisible.value = true
}

async function doSave() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    if (isEdit.value) await updateAddress(form.id, form)
    else await saveAddress(form)
    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    fetchData()
  } catch { /* */ }
}

async function doDelete(id) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  try { await deleteAddress(id); ElMessage.success('删除成功'); fetchData() } catch { /* */ }
}
</script>

<style scoped>
.toolbar-title { color: #17372e; font-size: 17px; font-weight: 900; }
.toolbar-subtitle { margin-top: 4px; color: #9a8d78; font-size: 13px; }
</style>

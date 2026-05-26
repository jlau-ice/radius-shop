<template>
  <div class="page-shell">
    <div class="page-heading">
      <div class="page-kicker">DELIVERY TEAM</div>
      <div class="page-title">配送员管理</div>
    </div>

    <div class="toolbar">
      <div>
        <div class="toolbar-title">配送团队</div>
        <div class="toolbar-subtitle">维护配送员账号，供配送小程序登录使用</div>
      </div>
      <el-button type="primary" @click="showDialog(null)">新增配送员</el-button>
    </div>

    <el-table :data="tableData" v-loading="loading">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="name" label="姓名" width="130" />
      <el-table-column prop="username" label="账号" width="160" />
      <el-table-column prop="phone" label="手机号" width="150" />
      <el-table-column label="状态" width="110">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="light">{{ row.status === 1 ? '在职' : '离职' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="170" align="right">
        <template #default="{ row }">
          <el-button size="small" @click="showDialog(row)">编辑</el-button>
          <el-button size="small" type="danger" plain @click="doDelete(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="isEdit ? '编辑配送员' : '新增配送员'" v-model="dialogVisible" width="520px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="84px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="账号" prop="username">
          <el-input v-model="form.username" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="密码" :prop="isEdit ? '' : 'password'">
          <el-input v-model="form.password" :placeholder="isEdit ? '留空不修改' : '请输入密码'" show-password />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
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
import { getDeliveryList, saveDelivery, updateDelivery, deleteDelivery } from '@/api/delivery'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref(null)
const form = reactive({ id: null, name: '', username: '', password: '', phone: '', status: 1 })
const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

onMounted(() => fetchData())

async function fetchData() {
  loading.value = true
  try { tableData.value = await getDeliveryList() } catch { /* */ }
  finally { loading.value = false }
}

function showDialog(row) {
  isEdit.value = !!row
  if (row) Object.assign(form, { ...row, password: '' })
  else Object.assign(form, { id: null, name: '', username: '', password: '', phone: '', status: 1 })
  dialogVisible.value = true
}

async function doSave() {
  if (isEdit.value && !form.password) delete rules.password
  else rules.password = [{ required: true, message: '请输入密码', trigger: 'blur' }]

  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  try {
    if (isEdit.value) await updateDelivery(form.id, form)
    else await saveDelivery(form)
    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    fetchData()
  } catch { /* */ }
}

async function doDelete(id) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  try { await deleteDelivery(id); ElMessage.success('删除成功'); fetchData() } catch { /* */ }
}
</script>

<style scoped>
.toolbar-title { color: #17372e; font-size: 17px; font-weight: 900; }
.toolbar-subtitle { margin-top: 4px; color: #9a8d78; font-size: 13px; }
</style>

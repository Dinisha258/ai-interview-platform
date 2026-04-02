<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="归属的面试场次ID" prop="sessionId">
        <el-input
          v-model="queryParams.sessionId"
          placeholder="请输入归属的面试场次ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <!-- 以下四项已删除 -->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['aip:dialogue:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['aip:dialogue:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['aip:dialogue:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['aip:dialogue:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="dialogueList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="对话主键ID" align="center" prop="id" />
      <el-table-column label="归属的面试场次ID" align="center" prop="sessionId" />
      <!-- 以下四列已删除：轮次序号、是否为AI发起的追问、学生答题录音文件路径、单点打分 -->
      <el-table-column label="AI抛出的问题" align="center" prop="aiContent" />
      <el-table-column label="学生回答的文本" align="center" prop="userContent" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['aip:dialogue:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['aip:dialogue:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改问答对话明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="归属的面试场次ID" prop="sessionId">
          <el-input v-model="form.sessionId" placeholder="请输入归属的面试场次ID" />
        </el-form-item>
        <!-- 以下四项已删除：轮次序号、是否为AI发起的追问、学生答题录音文件路径、单点打分 -->
        <el-form-item label="AI抛出的问题">
          <editor v-model="form.aiContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="学生回答的文本">
          <editor v-model="form.userContent" :min-height="192"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDialogue, getDialogue, delDialogue, addDialogue, updateDialogue } from "@/api/aip/dialogue"

export default {
  name: "Dialogue",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 问答对话明细表格数据
      dialogueList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数（已删除 roundNum, isFollowUp, audioUrl, turnScore）
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sessionId: null,
        aiContent: null,
        userContent: null,
      },
      // 表单参数
      form: {},
      // 表单校验（已删除 roundNum 的必填校验）
      rules: {
        // 可根据需要添加其他校验，例如 sessionId 等
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询问答对话明细列表 */
    getList() {
      this.loading = true
      listDialogue(this.queryParams).then(response => {
        this.dialogueList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置（已删除相应字段）
    reset() {
      this.form = {
        id: null,
        sessionId: null,
        aiContent: null,
        userContent: null,
        createTime: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加问答对话明细"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDialogue(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改问答对话明细"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDialogue(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDialogue(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除问答对话明细编号为"' + ids + '"的数据项？').then(function() {
        return delDialogue(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('aip/dialogue/export', {
        ...this.queryParams
      }, `dialogue_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px" class="inline-form">
      <el-form-item label="关联的岗位" prop="positionId">
        <el-select
          v-model="queryParams.positionId"
          placeholder="请选择岗位"
          clearable
          filterable
          @keyup.enter.native="handleQuery"
          style="width: 180px"
        >
          <el-option
            v-for="item in postOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="难度系数" prop="difficulty">
        <el-select
          v-model="queryParams.difficulty"
          placeholder="请选择难度"
          clearable
          @keyup.enter.native="handleQuery"
          style="width: 150px"
        >
          <el-option label="1" :value="1" />
          <el-option label="2" :value="2" />
          <el-option label="3" :value="3" />
          <el-option label="4" :value="4" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 添加 button-row 类以增加上边距 -->
    <el-row :gutter="10" class="mb8 button-row">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['aip:question:add']"
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
          v-hasPermi="['aip:question:edit']"
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
          v-hasPermi="['aip:question:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['aip:question:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="questionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="题目主键ID" align="center" prop="id" />
      <el-table-column label="关联的岗位ID" align="center" prop="positionId" />
      <el-table-column label="题目分类" align="center" prop="type" />
      <el-table-column label="题目内容" align="center" prop="content" />
      <el-table-column label="参考答案及踩分点" align="center" prop="answer" />
      <el-table-column label="难度系数" align="center" prop="difficulty" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['aip:question:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['aip:question:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="关联的岗位" prop="positionId">
          <el-select
            v-model="form.positionId"
            placeholder="请选择岗位"
            clearable
            filterable
            style="width: 100%"
          >
            <el-option
              v-for="item in postOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="题目内容" prop="content">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="答案及踩分点" prop="answer">
          <el-input v-model="form.answer" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="难度系数" prop="difficulty">
          <el-select v-model="form.difficulty" placeholder="请选择难度" clearable style="width: 100%">
            <el-option label="1" :value="1" />
            <el-option label="2" :value="2" />
            <el-option label="3" :value="3" />
            <el-option label="4" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listQuestion, getQuestion, delQuestion, addQuestion, updateQuestion } from "@/api/aip/question"
import { listPost } from "@/api/system/post"

export default {
  name: "Question",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      questionList: [],
      title: "",
      open: false,
      postOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        positionId: null,
        type: null,
        content: null,
        answer: null,
        difficulty: null,
      },
      form: {},
      rules: {
        content: [
          { required: true, message: "题目内容不能为空", trigger: "blur" }
        ],
        difficulty: [
          { required: true, message: "请选择难度系数", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
    this.getPostList()
  },
  methods: {
    getList() {
      this.loading = true
      listQuestion(this.queryParams).then(response => {
        this.questionList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getPostList() {
      listPost({ pageSize: 100, status: '0' }).then(response => {
        this.postOptions = response.rows.map(item => ({
          value: item.postId,
          label: item.postName
        }))
      }).catch(() => {
        this.postOptions = []
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null,
        positionId: null,
        type: null,
        content: null,
        answer: null,
        difficulty: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加面试题目库"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getQuestion(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改面试题目库"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateQuestion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addQuestion(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除面试题目库编号为"' + ids + '"的数据项？').then(() => {
        return delQuestion(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('aip/question/export', {
        ...this.queryParams
      }, `question_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.inline-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}
.inline-form .el-form-item {
  margin-right: 15px;
  margin-bottom: 0;
}
/* 确保标签文字不换行 */
.inline-form .el-form-item__label {
  white-space: nowrap;
}
/* 增加按钮行与查询表单之间的纵向距离 */
.button-row {
  margin-top: 15px;
}
</style>

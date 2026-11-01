<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="岗位名称" prop="positionName">
        <el-input
          v-model="queryParams.positionName"
          placeholder="请输入岗位名称"
          clearable
          style="width: 180px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 工具栏 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-refresh" size="mini" :loading="listLoading" @click="getList">刷新</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 报告列表 -->
    <el-table v-loading="listLoading" :data="reportList">
      <el-table-column label="岗位名称" align="center" prop="positionName" />
      <el-table-column label="目标等级" align="center" prop="targetLevel">
        <template slot-scope="scope">
          <el-tag size="mini">{{ levelName(scope.row.targetLevel) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="综合得分" align="center" prop="totalScore" width="100">
        <template slot-scope="scope">
          <span :class="scoreClass(scope.row.totalScore)">{{ formatScore(scope.row.totalScore) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="面试结束时间" align="center" prop="endTime" width="180" />
      <el-table-column label="报告生成时间" align="center" prop="createTime" width="180" />
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看详情</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['aip:report:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 报告详情弹窗(复用聊天页的报告组件) -->
    <ReportDetail :visible.sync="showDetail" :report="currentReport" />
  </div>
</template>

<script>
import { listReport, delReport } from '@/api/aip/report'
import ReportDetail from '@/components/ReportDetail/index.vue'

export default {
  name: 'Report',
  components: { ReportDetail },
  data() {
    return {
      // 遮罩层
      listLoading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 报告表格数据
      reportList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        positionName: null
      },
      // 详情弹窗
      showDetail: false,
      currentReport: null
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 查询报告列表
    getList() {
      this.listLoading = true
      listReport(this.queryParams).then(response => {
        this.reportList = response.rows
        this.total = response.total
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    // 搜索按钮操作
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    // 重置按钮操作
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 查看详情
    handleView(row) {
      this.currentReport = row
      this.showDetail = true
    },
    // 删除按钮操作
    handleDelete(row) {
      this.$confirm('确定删除该面试报告吗?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return delReport(row.id)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {})
    },
    // 等级数字转名称
    levelName(level) {
      const map = { 1: '实习生', 2: '初级', 3: '中级', 4: '高级' }
      return map[level] || '未知'
    },
    // 分数格式化(无分数时显示——)
    formatScore(score) {
      if (score == null) return '—'
      return Number(score).toFixed(1)
    },
    // 分数颜色
    scoreClass(score) {
      const s = Number(score || 0)
      if (s >= 80) return 'score-good'
      if (s >= 60) return 'score-mid'
      return 'score-low'
    }
  }
}
</script>

<style scoped>
.score-good { color: #67c23a; font-weight: bold; }
.score-mid { color: #e6a23c; font-weight: bold; }
.score-low { color: #f56c6c; font-weight: bold; }
</style>

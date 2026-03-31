<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24" style="padding-left: 20px; text-align: center">
        <h1 class="hero-title">王邯牛逼</h1>
        <div class="hero-image">
          <img
            src="https://img1.baidu.com/it/u=3616291828,1966019214&fm=253&app=138&f=JPEG?w=800&h=907"
            alt="王邯牛逼"
            class="center-image"
          />
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Index",
  data() {
    return {
      // 版本号
      version: "3.9.2"
    }
  },
  methods: {
    goTarget(href) {
      window.open(href, "_blank")
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  overflow-x: hidden;
  min-height: calc(100vh - 120px);
  display: flex;
  align-items: center;
  justify-content: center;

  .hero-title {
    font-size: 72px;
    font-weight: bold;
    color: #ff6b6b;
    text-shadow: 4px 4px 8px rgba(0, 0, 0, 0.2);
    margin-bottom: 40px;
    letter-spacing: 8px;
    animation: fadeInDown 0.8s ease-out;
  }

  .hero-image {
    display: flex;
    justify-content: center;
    animation: fadeInUp 0.8s ease-out;

    .center-image {
      max-width: 500px;
      width: 100%;
      height: auto;
      border-radius: 20px;
      box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
      transition: transform 0.3s ease;

      &:hover {
        transform: scale(1.02);
      }
    }
  }
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>

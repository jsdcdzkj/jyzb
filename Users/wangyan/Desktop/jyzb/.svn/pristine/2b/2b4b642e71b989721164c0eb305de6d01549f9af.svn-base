<template>
  <div class="navbar">
    <breadcrumb class="breadcrumb-container" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from '@/components/Breadcrumb'
export default {
  components: {
    Breadcrumb
  },
  computed: {
    ...mapGetters(["sidebar", "avatar"]),
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("app/toggleSideBar");
    },
  }
}
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';
.navbar {
  height: 35px;
  line-height:35px;
  overflow: hidden;
  position: relative;
  display:flex;
  background: #fff;
  border-radius: 4px;
  margin-bottom: 10px;
}
</style>

<template>
  <div>
    <svg-icon icon-class="github" @click="goto"/>
  </div>
</template>

<script>
export default {
  name: 'LuckinCRMGit',
  data() {
    return {
      url: 'https://gitee.com/mrbyte/luckin-crm'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
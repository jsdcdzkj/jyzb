<template>
    <div :id="id" :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import echarts from 'echarts'
import resize from './mixins/resize'

export default {
    mixins: [resize],
    props: {
        className: {
            type: String,
            default: 'chart'
        },
        id: {
            type: String,
            default: 'chart'
        },
        width: {
            type: String,
            default: '100%'
        },
        height: {
            type: String,
            default: '600px'
        },
        valdata: {
            type: Object,
            default: () => {
                return {
                    Xdata: [],
                    legendData: [],
                    leftValue: [

                    ],
                    rightValue: [

                    ],
                };
            }
        }
    },
    data() {
        return {
            chart: null
        }
    },
    mounted() {
        this.initChart()
    },
    watch:{
        valdata(){
            this.initChart();
        }
    },
    beforeDestroy() {
        if (!this.chart) {
            return
        }
        this.chart.dispose()
        this.chart = null
    },
    methods: {
        initChart() {
            this.chart = echarts.init(document.getElementById(this.id))
            //图表X轴数据
            var Xdata = this.valdata.Xdata;

            //图表项目
            var fp_coler = ['#409EFF', '#EC8A5D', '#67C23A', '#DE6E6A', '#5A6FC0', '#F2CA6B', '#85BEDB', '#9263AF'];

            var legendData = this.valdata.legendData;
            var leftValue = this.valdata.leftValue
            var rightValue = this.valdata.rightValue
            const seriesList = [];
            legendData.forEach((item, i) => {
                seriesList.push({
                    name: legendData[i],
                    type: 'bar',
                    stack: 'one',
                    barGap: 10,
                    barWidth: 24,
                    label: {
                        normal: {
                            show: true,
                            position: 'inside',
                            textStyle: {
                                color: '#fff',
                                fontSize: 12,
                            },
                            formatter: function(params) {
                                if (params.value === 0) {
                                    return '';
                                }
                                return params.value;
                            }
                        },
                        emphasis: {
                            show: true,
                            position: 'inside',
                            offset: [0, 0],
                            textStyle: {
                                color: '#fff',
                                fontSize: 14,
                            },
                        },
                    },
                    itemStyle: {
                        normal: {
                            color: function (params) {
                                return fp_coler[i]
                            },
                            opacity: 1,

                        },
                        emphasis: {
                            opacity: 1,
                        },
                    },
                    data: leftValue[i],
                });
                seriesList.push({
                    name: legendData[i],
                    stack: 'right',
                    type: 'bar',
                    barGap: 10,
                    barWidth: 24,
                    xAxisIndex: 2,
                    yAxisIndex: 2,
                    label: {
                        normal: {
                            show: true,
                            position: 'inside',
                            textStyle: {
                                color: '#fff',
                                fontSize: 14,
                            },
                            formatter: function(params) {
                                if (params.value === 0) {
                                    return '';
                                }
                                return params.value;
                            }
                        },
                        emphasis: {
                            show: true,
                            position: 'inside',
                            offset: [0, 0],
                            textStyle: {
                                color: '#fff',
                                fontSize: 14,
                            },
                        },
                    },
                    itemStyle: {
                        normal: {
                            color: function (params) {
                                return fp_coler[i]
                            },
                            opacity: 1,

                        },
                        emphasis: {
                            opacity: 1,
                        },
                    },
                    data: rightValue[i],
                });
            });
            this.chart.setOption({
                color: fp_coler,
                legend: {
                    data: legendData,
                    icon: 'circle',
                    textStyle: {
                        color: '#666',
                        fontSize: 14,
                    },
                    itemWidth: 10,
                    itemHeight: 10,
                    borderRadius: 2,
                    backgroundColor: 'rgba(255,255,255, .05)',
                    itemGap: 12,
                    padding: [4, 8],
                    bottom: 18,
                },
                tooltip: {
                    show: true,
                    trigger: 'axis',
                    //formatter: '{b}<br/>{a}: {c}人',
                    axisPointer: {
                        type: 'shadow',
                    }
                },
                grid: [{
                    show: false,
                    left: "2%",
                    top: 14,
                    bottom: 60,
                    containLabel: true,
                    width: '41%',
                }, {
                    show: false,
                    left: '50.5%',
                    top: 14,
                    bottom: 60,
                    width: '14%',
                }, {
                    show: false,
                    right: "2%",
                    top: 14,
                    bottom: 60,
                    containLabel: true,
                    width: '43%',
                },],

                xAxis: [
                    {
                        type: 'value',
                        triggerEvent: true,
                        inverse: true,
                        axisLine: {
                            show: false,
                        },
                        axisTick: {
                            show: false,
                        },
                        position: 'top',
                        axisLabel: {
                            show: false,
                            textStyle: {
                                color: '#B2B2B2',
                                fontSize: 12,
                            },
                        },
                        splitLine: {
                            show: false,
                            lineStyle: {
                                color: '#1F2022',
                                width: 1,
                                type: 'solid',
                            },
                        },
                    },
                    {
                        gridIndex: 1,
                        show: false,
                    },
                    {
                        gridIndex: 2,
                        type: 'value',
                        axisLine: {
                            show: false,
                        },
                        axisTick: {
                            show: false,
                        },
                        position: 'top',
                        axisLabel: {
                            show: false,
                            textStyle: {
                                color: '#B2B2B2',
                                fontSize: 12,
                            },
                        },
                        splitLine: {
                            show: false,
                            lineStyle: {
                                color: '#1F2022',
                                width: 1,
                                type: 'solid',
                            },
                        },
                    }
                ],
                yAxis: [{
                    type: 'category',
                    inverse: true,
                    position: 'right',
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        show: false,

                    },
                    data: Xdata,
                }, {
                    gridIndex: 1,
                    type: 'category',
                    inverse: true,
                    position: 'left',
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        show: true,
                        textStyle: {
                            color: '#333',
                            fontSize: 12,
                            backgroundColor: '#F8F8F8',
                            padding: [6, 22],
                            borderRadius: 24,
                        },

                    },
                    data: Xdata.map(function (value) {
                        return {
                            value: value,
                            textStyle: {
                                align: 'center',
                            }
                        }
                    }),
                }, {
                    gridIndex: 2,
                    type: 'category',
                    inverse: true,
                    position: 'left',
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        show: false,
                        textStyle: {
                            color: '#9D9EA0',
                            fontSize: 12,
                        },

                    },
                    data: Xdata,
                },],
                series: seriesList
            })
        }
    }
}
</script>
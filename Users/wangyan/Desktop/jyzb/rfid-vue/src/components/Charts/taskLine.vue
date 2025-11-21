<template>
    <div :class="className" :style="{ height: height, width: width }" />
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
        width: {
            type: String,
            default: '100%'
        },
        height: {
            type: String,
            default: '390px'
        },
        valdata: {
            type: Object,
            default: () => {
                return {
                    legendData: [],
                    xLabel: [],
                    dataNumA: [],
                    dataNumB: [],
                    dataNumC: [],
                }
            }
        }
    },
    data() {
        return {
            chart: null
        }
    },
    watch: {
        valdata() {
            this.initChart()
        },
    },
    mounted() {
        setTimeout(() => {
            this.$nextTick(() => {
            this.initChart()
        });
        }, 20);
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
            this.chart = echarts.init(this.$el)
            this.chart.setOption({
                grid: {
                    left: '10px',
                    right: '30px',
                    bottom: '10px',
                    top:'60px',
                    containLabel: true
                },
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'cross',
                        crossStyle: {
                            color: '#999'
                        }
                    }
                },
                legend: {
                    data: this.valdata.legendData,
                    icon:'circle',
                    textStyle: {
                        color: '#666',
                        fontSize: 12,
                    },
                    itemWidth: 10,
                    itemHeight: 10,
                    borderRadius: 2,
                    backgroundColor: 'rgba(255,255,255, .05)',
                    itemGap: 12,
                    padding: [4, 8],
                    right: "4%",
                    top: "0",
                },
                xAxis: [
                    {
                        type: 'category',
                        axisTick: {
                          show: false,
                        },
                        boundaryGap: false,
                        interval: 1,
                        axisLabel: {
                            color: '#666',
                            fontSize: 12
                        },
                        axisLine: {
                            show: true,
                            lineStyle: {
                              type:'solide',
                              color: 'rgba(131, 139, 150, 0.24)',
                            }
                        },
                        data: this.valdata.xLabel,
                    }
                ],
                yAxis: [
                    {
                        type: 'value',
                        name: '',
                        minInterval:1,
                        axisLabel: {
                            formatter: '{value} '
                        },
                        axisLine: {
                            show: false,
                            lineStyle: {
                                color: '#21385c',
                            }
                        },
                        axisTick: {
                            show: false,
                        },
                        axisLabel: {
                            textStyle: {
                                //坐标轴颜色
                                color: '#666',
                                fontSize: 12
                            }
                        },
                        //坐标轴线样式
                        splitLine: {
                            show: true,
                            lineStyle: {
                                type: 'dashed', //solid实线;dashed虚线
                                color: 'rgba(131, 139, 150, 0.24)'
                            }
                        },
                    }
                ],
                series: [
                {
                        name: this.valdata.legendData[2],
                        data: this.valdata.dataNumC,
                        type: 'line',
                        symbol: 'image://'+require('@/assets/layout/line_yellow.png'), // 默认是空心圆（中间是白色的），改成实心圆
                        symbolSize: 12,
                        lineStyle: {
                            normal: {
                                width: 1,
                                color: "rgba(234, 178, 95, 1)", // 线条颜色
                            },
                            borderColor: 'rgba(0,0,0,.4)',
                        },
                        itemStyle: {
                            color: "rgba(234, 178, 95, 1)",
                            borderColor: "rgba(234, 178, 95, 1)",
                            borderWidth: 2,
                            shadowColor: 'rgba(22, 137, 229)',
                            shadowBlur: 0
                        },
                        tooltip: {
                            show: true
                        },
                        areaStyle: { //区域填充样式
                            normal: {
                                //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0,
                                    color: "rgba(234, 178, 95,.6)"
                                },
                                {
                                    offset: 1,
                                    color: "rgba(234, 178, 95, 0)"
                                }
                                ], false),
                                shadowColor: 'rgba(234, 178, 95, 0.5)', //阴影颜色
                                shadowBlur: 20 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
                            }
                        },
                    },
                    {
                        name: this.valdata.legendData[1],
                        data: this.valdata.dataNumB,
                        type: 'line',
                        symbol: 'image://'+require('@/assets/layout/line_green.png'), // 默认是空心圆（中间是白色的），改成实心圆
                        symbolSize: 12,
                        lineStyle: {
                            normal: {
                                width: 1,
                                color: "rgba(147, 216, 113, 1)", // 线条颜色
                            },
                            borderColor: 'rgba(0,0,0,.4)',
                        },
                        itemStyle: {
                            color: "rgba(147, 216, 113, 1)",
                            borderColor: "#93D871",
                            borderWidth: 2,
                            shadowColor: 'rgba(22, 137, 229)',
                            shadowBlur: 0
                        },
                        tooltip: {
                            show: true
                        },
                        areaStyle: { //区域填充样式
                            normal: {
                                //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0,
                                    color: "rgba(147, 216, 113,.6)"
                                },
                                {
                                    offset: 1,
                                    color: "rgba(147, 216, 113, 0)"
                                }
                                ], false),
                                shadowColor: 'rgba(147, 216, 113,0.5)', //阴影颜色
                                shadowBlur: 20 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
                            }
                        },
                    },
                    {
                        name: this.valdata.legendData[0],
                        data: this.valdata.dataNumA,
                        type: 'line',
                        symbol: 'image://'+require('@/assets/layout/line_blue.png'), // 默认是空心圆（中间是白色的），改成实心圆
                        symbolSize: 12,
                        itemStyle: {
                            normal: {
                                color: '#409EFF', //改变折线点的颜色
                                lineStyle: {
                                    color: '#409EFF', //改变折线颜色
                                    type: 'solid'
                                }
                            }
                        },
                        lineStyle: {
                            normal: {
                                width: 1,
                                color: "#409EFF", // 线条颜色
                            },
                            borderColor: 'rgba(0,0,0,.4)',
                        },
                        tooltip: {
                            show: true
                        },
                        areaStyle: { //区域填充样式
                            normal: {
                                //线性渐变，前4个参数分别是x0,y0,x2,y2(范围0~1);相当于图形包围盒中的百分比。如果最后一个参数是‘true’，则该四个值是绝对像素位置。
                                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                                    offset: 0,
                                    color: "rgba(120, 171, 224, .6)"
                                },
                                {
                                    offset: 1,
                                    color: "rgba(120, 171, 224, 0)"
                                }
                                ], false),
                                shadowColor: 'rgba(120, 171, 224,0.5)', //阴影颜色
                                shadowBlur: 0 //shadowBlur设图形阴影的模糊大小。配合shadowColor,shadowOffsetX/Y, 设置图形的阴影效果。
                            }
                        },
                    }
                ]
            })
            this.chart.on('click', () =>{  //这里使用箭头函数代替function，this指向VueComponent
                this.$emit('child-event');
            });
        },

    }
}
</script>

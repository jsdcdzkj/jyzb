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
            default: "chart"
        },
        width: {
            type: String,
            default: "100%"
        },
        height: {
            type: String,
            default: '298px'
        },
        valdata: {
            type: Object,
            default:{
                equipTypeList:[],
                total:'',
                title:''
            }
        }
    },
    data() {
        return {
            chart: null
        };
    },
    watch: {
        valdata(val) {
            this.initChart();
        }
    },
    mounted() {
        this.$nextTick(() => {
            this.initChart();
        });
    },
    beforeDestroy() {
        if (!this.chart) {
            return;
        }
        this.chart.dispose();
        this.chart = null;
    },
    methods: {
        initChart() {
            var colorList = ["#409EFF", "#EC8A5D", "#67C23A", "#DE6E6A", "#5A6FC0", "#F2CA6B", "#85BEDB", "#9263AF"];
            this.chart = echarts.init(this.$el);
            var scaleData = this.valdata.equipTypeList;
            var data = [];
            for (var i = 0; i < scaleData.length; i++) {
                data.push(
                    {
                        data: scaleData[i].equipNum,
                        value: scaleData[i].equipNum,
                        name: scaleData[i].equipType,
                        itemStyle: {
                            normal: {
                                color: colorList[i]
                            }
                        }
                    }
                );
            }
            const legendData = this.valdata.equipTypeList.map(item => item.equipType)
            this.chart.setOption({
                color: colorList,
                legend: {
                    type: "scroll",
                    orient: "vertical",
                    right: "5%",
                    top: "center",
                    itemGap: 14,
                    icon: "circle",
                    data: legendData,
                    textStyle: {
                        color: "#5E707E",
                        rich: {
                            uname: {
                                align: "left"
                            },
                            unum: {
                                fontSize: 12,
                                padding:[0,5,0,5],
                                color: "#333",
                                fontWeight: "bold",
                                align: "right"
                            }
                        }
                    },
                    formatter: (name) => {
                        let info = scaleData.find(el=>el.equipType==name);
                        return `{uname|${name}} {unum|${info.rate}} {unum|${info.equipNum}}`;
                    },
                },
                tooltip: {
                    show: true,
                    formatter: "{b}{c}个 \t{d}%"
                },
                series: [
                    // 外边设置
                    // {
                    //     type: "pie",
                    //     center: ["30%", "50%"],
                    //     radius: ["55%", "62%"],
                    //     itemStyle: {
                    //         color: "#E5ECF0"
                    //     },
                    //     label: {
                    //         show: false
                    //     },
                    //     data: [0]
                    // },
                    // 展示层
                    {
                        type: "pie",
                        center: ["30%", "50%"],
                        radius: ["35%", "60%"],
                        // minAngle:10,
                        itemStyle: {
                            borderWidth: data.filter(el=>el.value>0).length>1?4:0, //描边线宽
                            borderColor: "#ffffff"
                        },
                        label: {
                            position: 'center',
                            formatter: () => {
                                return '{total|'+this.valdata.total+'}\r\n'+this.valdata.title;
                            },
                            rich: {
                                total: {
                                fontSize: 20,
                                color: '#333',
                                lineHeight: 30,
                                },
                            },
                            color: '#7a8c9f',
                            fontSize: 12,
                            lineHeight: 14,
                        },
                        labelLine: {
                            show: false,
                            normal: {
                                length: 40,
                                length2: 85,
                                align: "right",
                                lineStyle: {
                                    width: 1
                                }
                            }
                        },
                        data: data
                    },
                    // 外边框虚线
                    /* {
                        type: "pie",
                        zlevel: 4,
                        silent: true,
                        center: ["30%", "50%"],
                        radius: ["67%", "72%"],
                        label: {
                            show: false
                        },
                        labelLine: {
                            show: false
                        },
                        data: this.createData()
                    } */
                ]
            });
        },
        createData() {
            let dataArr = [];
            for (var i = 0; i < 200; i++) {
                if (i % 2 === 0) {
                    dataArr.push({
                        name: (i + 1).toString(),
                        value: 25,
                        itemStyle: {
                            normal: {
                                color: "#E5ECF0",
                                borderWidth: 0,
                                borderColor: "rgba(0,0,0,0)"
                            }
                        }
                    });
                } else {
                    dataArr.push({
                        name: (i + 1).toString(),
                        value: 20,
                        itemStyle: {
                            normal: {
                                color: "rgba(0,0,0,0)",
                                borderWidth: 0,
                                borderColor: "rgba(0,0,0,0)"
                            }
                        }
                    });
                }
            }
            return dataArr;
        }
    }
};
</script>

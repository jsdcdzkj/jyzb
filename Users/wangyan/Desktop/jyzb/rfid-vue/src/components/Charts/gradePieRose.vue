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
            default: '356px'
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
            let data = [];
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
                    right: "10",
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
                    // 展示层
                    {
                        type: "pie",
                        center: ["26%", "50%"],
                        radius: ["35%", "60%"],
                        // roseType: "radius",
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
                        itemStyle: {
                            borderWidth: data.filter(el=>el.value>0).length>1?4:0, //描边线宽
                            borderColor: "#ffffff"
                        },
                        labelLine: {
                            show: false,
                        },
                        data
                    }
                ]
            });
        }
    }
};
</script>

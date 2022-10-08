let api = [];
const apiDocListSize = 1
api.push({
    name: 'default',
    order: '1',
    list: []
})
api[0].list.push({
    alias: 'AppInfoController',
    order: '1',
    link: '应用信息',
    desc: '应用信息',
    list: []
})
api[0].list[0].list.push({
    order: '1',
    deprecated: 'false',
    url: '/version',
    desc: '系统版本号',
});
api[0].list.push({
    alias: 'LogController',
    order: '2',
    link: '日志下载',
    desc: '日志下载',
    list: []
})
api[0].list[1].list.push({
    order: '1',
    deprecated: 'false',
    url: '/log/list',
    desc: '日志列表',
});
api[0].list[1].list.push({
    order: '2',
    deprecated: 'false',
    url: '/log/get',
    desc: '日志下载',
});
api[0].list.push({
    alias: 'FileRecordController',
    order: '3',
    link: '文件管理',
    desc: '文件管理',
    list: []
})
api[0].list[2].list.push({
    order: '1',
    deprecated: 'false',
    url: '/file/404',
    desc: '文件未找到',
});
api[0].list[2].list.push({
    order: '2',
    deprecated: 'false',
    url: '/file/img',
    desc: '查看图片',
});
api[0].list[2].list.push({
    order: '3',
    deprecated: 'false',
    url: '/file/download',
    desc: '下载文件',
});
api[0].list.push({
    alias: 'LoginController',
    order: '4',
    link: '登录',
    desc: '登录',
    list: []
})
api[0].list[3].list.push({
    order: '1',
    deprecated: 'false',
    url: '/login',
    desc: '',
});
api[0].list[3].list.push({
    order: '2',
    deprecated: 'false',
    url: '/logout',
    desc: '',
});
api[0].list.push({
    alias: 'SysConfigController',
    order: '5',
    link: '参数管理',
    desc: '参数管理',
    list: []
})
api[0].list[4].list.push({
    order: '1',
    deprecated: 'false',
    url: '/sys/config/list',
    desc: '列表',
});
api[0].list[4].list.push({
    order: '2',
    deprecated: 'false',
    url: '/sys/config/saveOrUpdate',
    desc: '新增或修改',
});
api[0].list.push({
    alias: 'SysDeptController',
    order: '6',
    link: '部门管理',
    desc: '部门管理',
    list: []
})
api[0].list[5].list.push({
    order: '1',
    deprecated: 'false',
    url: '/sys/dept/list',
    desc: '',
});
api[0].list[5].list.push({
    order: '2',
    deprecated: 'false',
    url: '/sys/dept/{id}',
    desc: '',
});
api[0].list[5].list.push({
    order: '3',
    deprecated: 'false',
    url: '/sys/dept/',
    desc: '',
});
api[0].list[5].list.push({
    order: '4',
    deprecated: 'false',
    url: '/sys/dept/',
    desc: '',
});
api[0].list[5].list.push({
    order: '5',
    deprecated: 'false',
    url: '/sys/dept/{id}',
    desc: '',
});
api[0].list.push({
    alias: 'SysMenuController',
    order: '7',
    link: '菜单管理',
    desc: '菜单管理',
    list: []
})
api[0].list[6].list.push({
    order: '1',
    deprecated: 'false',
    url: '/sys/menu/nav',
    desc: '',
});
api[0].list[6].list.push({
    order: '2',
    deprecated: 'false',
    url: '/sys/menu/permissions',
    desc: '',
});
api[0].list[6].list.push({
    order: '3',
    deprecated: 'false',
    url: '/sys/menu/list',
    desc: '',
});
api[0].list[6].list.push({
    order: '4',
    deprecated: 'false',
    url: '/sys/menu/{id}',
    desc: '',
});
api[0].list[6].list.push({
    order: '5',
    deprecated: 'false',
    url: '/sys/menu/',
    desc: '',
});
api[0].list[6].list.push({
    order: '6',
    deprecated: 'false',
    url: '/sys/menu/',
    desc: '',
});
api[0].list[6].list.push({
    order: '7',
    deprecated: 'false',
    url: '/sys/menu/{id}',
    desc: '',
});
api[0].list[6].list.push({
    order: '8',
    deprecated: 'false',
    url: '/sys/menu/select',
    desc: '',
});
api[0].list.push({
    alias: 'SysRoleController',
    order: '8',
    link: '角色管理',
    desc: '角色管理',
    list: []
})
api[0].list[7].list.push({
    order: '1',
    deprecated: 'false',
    url: '/sys/role/page',
    desc: '',
});
api[0].list[7].list.push({
    order: '2',
    deprecated: 'false',
    url: '/sys/role/list',
    desc: '',
});
api[0].list[7].list.push({
    order: '3',
    deprecated: 'false',
    url: '/sys/role/{id}',
    desc: '',
});
api[0].list[7].list.push({
    order: '4',
    deprecated: 'false',
    url: '/sys/role/',
    desc: '',
});
api[0].list[7].list.push({
    order: '5',
    deprecated: 'false',
    url: '/sys/role/',
    desc: '',
});
api[0].list[7].list.push({
    order: '6',
    deprecated: 'false',
    url: '/sys/role/',
    desc: '',
});
api[0].list.push({
    alias: 'SysUserController',
    order: '9',
    link: '用户管理',
    desc: '用户管理',
    list: []
})
api[0].list[8].list.push({
    order: '1',
    deprecated: 'false',
    url: '/sys/user/page',
    desc: '',
});
api[0].list[8].list.push({
    order: '2',
    deprecated: 'false',
    url: '/sys/user/{id}',
    desc: '',
});
api[0].list[8].list.push({
    order: '3',
    deprecated: 'false',
    url: '/sys/user/info',
    desc: '',
});
api[0].list[8].list.push({
    order: '4',
    deprecated: 'false',
    url: '/sys/user/password',
    desc: '',
});
api[0].list[8].list.push({
    order: '5',
    deprecated: 'false',
    url: '/sys/user/',
    desc: '',
});
api[0].list[8].list.push({
    order: '6',
    deprecated: 'false',
    url: '/sys/user/',
    desc: '',
});
api[0].list[8].list.push({
    order: '7',
    deprecated: 'false',
    url: '/sys/user/',
    desc: '',
});
api[0].list.push({
    alias: 'ApiController',
    order: '10',
    link: '对外开放接口',
    desc: '对外开放接口',
    list: []
})
api[0].list[9].list.push({
    order: '1',
    deprecated: 'false',
    url: '/pushStatus',
    desc: '设备端数据推送接口',
});
api[0].list.push({
    alias: 'EquipmentController',
    order: '11',
    link: '设备管理',
    desc: '设备管理',
    list: []
})
api[0].list[10].list.push({
    order: '1',
    deprecated: 'false',
    url: '/equipment/guideDeviceStatus',
    desc: '诱导设备状态枚举',
});
api[0].list[10].list.push({
    order: '2',
    deprecated: 'false',
    url: '/equipment/powerBoxStatus',
    desc: '配电机箱状态枚举',
});
api[0].list[10].list.push({
    order: '3',
    deprecated: 'false',
    url: '/equipment/cameraStatus',
    desc: '摄像机状态枚举',
});
api[0].list[10].list.push({
    order: '4',
    deprecated: 'false',
    url: '/equipment/cameraType',
    desc: '摄像机类型枚举',
});
api[0].list[10].list.push({
    order: '5',
    deprecated: 'false',
    url: '/equipment/list',
    desc: '设备列表',
});
api[0].list[10].list.push({
    order: '6',
    deprecated: 'false',
    url: '/equipment/detail',
    desc: '设备详情',
});
api[0].list[10].list.push({
    order: '7',
    deprecated: 'false',
    url: '/equipment/save',
    desc: '添加设备',
});
api[0].list[10].list.push({
    order: '8',
    deprecated: 'false',
    url: '/equipment/update',
    desc: '修改设备(web端)',
});
api[0].list[10].list.push({
    order: '9',
    deprecated: 'false',
    url: '/equipment/upload',
    desc: '设备状态上传',
});
api[0].list[10].list.push({
    order: '10',
    deprecated: 'false',
    url: '/equipment/delete/{id}',
    desc: '设备删除',
});
api[0].list[10].list.push({
    order: '11',
    deprecated: 'false',
    url: '/equipment/export',
    desc: '导出',
});
api[0].list[10].list.push({
    order: '12',
    deprecated: 'false',
    url: '/equipment/videoUrl',
    desc: '获取设备实时视频',
});
api[0].list[10].list.push({
    order: '13',
    deprecated: 'false',
    url: '/equipment/updateName',
    desc: '修改设备名称',
});
api[0].list[10].list.push({
    order: '14',
    deprecated: 'false',
    url: '/equipment/getPushPath',
    desc: '获取推送地址',
});
api[0].list[10].list.push({
    order: '15',
    deprecated: 'false',
    url: '/equipment/updatePushPath',
    desc: '修改推送地址',
});
api[0].list[10].list.push({
    order: '16',
    deprecated: 'false',
    url: '/equipment/updateNtp',
    desc: '修改时钟同步地址',
});
api[0].list[10].list.push({
    order: '17',
    deprecated: 'false',
    url: '/equipment/cameraInfo',
    desc: '获取摄像机信息',
});
api[0].list[10].list.push({
    order: '18',
    deprecated: 'false',
    url: '/equipment/updateCameraInfo/{id}',
    desc: '修改摄像机信息',
});
api[0].list[10].list.push({
    order: '19',
    deprecated: 'false',
    url: '/equipment/guideTestParking/{id}',
    desc: '诱导设备测试-车辆停靠/有人',
});
api[0].list[10].list.push({
    order: '20',
    deprecated: 'false',
    url: '/equipment/guideTestLeave/{id}',
    desc: '诱导设备测试-车辆驶离',
});
api[0].list[10].list.push({
    order: '21',
    deprecated: 'false',
    url: '/equipment/cameraVoice',
    desc: '获取语音文件',
});
api[0].list[10].list.push({
    order: '22',
    deprecated: 'false',
    url: '/equipment/cameraVoiceTest/{id}',
    desc: '测试语音',
});
api[0].list[10].list.push({
    order: '23',
    deprecated: 'false',
    url: '/equipment/uploadVoice',
    desc: '修改声音 应用所有设备',
});
api[0].list[10].list.push({
    order: '24',
    deprecated: 'false',
    url: '/equipment/uploadVoice/{id}',
    desc: '修改声音 应用当前设备',
});
api[0].list[10].list.push({
    order: '25',
    deprecated: 'false',
    url: '/equipment/updateIp',
    desc: '修改设备ip',
});
api[0].list[10].list.push({
    order: '26',
    deprecated: 'false',
    url: '/equipment/updateVideoPath',
    desc: '修改视频流地址',
});
api[0].list.push({
    alias: 'EquipmentRecordController',
    order: '12',
    link: '设备记录',
    desc: '设备记录',
    list: []
})
api[0].list[11].list.push({
    order: '1',
    deprecated: 'false',
    url: '/equipment/record/list',
    desc: '列表',
});
api[0].list[11].list.push({
    order: '2',
    deprecated: 'false',
    url: '/equipment/record/export',
    desc: '导出',
});
api[0].list.push({
    alias: 'EventController',
    order: '13',
    link: '告警事件',
    desc: '告警事件',
    list: []
})
api[0].list[12].list.push({
    order: '1',
    deprecated: 'false',
    url: '/event/code',
    desc: '告警事件类型枚举(异常类型)',
});
api[0].list[12].list.push({
    order: '2',
    deprecated: 'false',
    url: '/event/status',
    desc: '事件状态枚举',
});
api[0].list[12].list.push({
    order: '3',
    deprecated: 'false',
    url: '/event/list',
    desc: '告警事件列表',
});
api[0].list[12].list.push({
    order: '4',
    deprecated: 'false',
    url: '/event/upload',
    desc: '事件上传',
});
api[0].list[12].list.push({
    order: '5',
    deprecated: 'false',
    url: '/event/falseAlarm/{id}',
    desc: '事件误报',
});
api[0].list[12].list.push({
    order: '6',
    deprecated: 'false',
    url: '/event/export',
    desc: '导出',
});
api[0].list.push({
    alias: 'EventStatisticsController',
    order: '14',
    link: '事件统计',
    desc: '事件统计',
    list: []
})
api[0].list[13].list.push({
    order: '1',
    deprecated: 'false',
    url: '/event/statistics/total',
    desc: '事件总次数',
});
api[0].list[13].list.push({
    order: '2',
    deprecated: 'false',
    url: '/event/statistics/totalList',
    desc: '各类型事件次数统计',
});
api[0].list[13].list.push({
    order: '3',
    deprecated: 'false',
    url: '/event/statistics/eventTotalRanking',
    desc: '易发设备排行',
});
api[0].list[13].list.push({
    order: '4',
    deprecated: 'false',
    url: '/event/statistics/realTimeData',
    desc: '实时数据',
});
api[0].list[13].list.push({
    order: '5',
    deprecated: 'false',
    url: '/event/statistics/equipmentAlarmCountRanking',
    desc: '根据事件类型查询告警易发设备',
});
api[0].list[13].list.push({
    order: '6',
    deprecated: 'false',
    url: '/event/statistics/equipmentFalseAlarmCountRanking',
    desc: '根据事件类型查询误报易发设备',
});
api[0].list[13].list.push({
    order: '7',
    deprecated: 'false',
    url: '/event/statistics/alarmTimeRanking',
    desc: '告警易发时间 3小时一段',
});
api[0].list[13].list.push({
    order: '8',
    deprecated: 'false',
    url: '/event/statistics/falseAlarmTimeRanking',
    desc: '误报易发时间 1小时一段',
});
api[0].list[13].list.push({
    order: '9',
    deprecated: 'false',
    url: '/event/statistics/todayYesterdayEventStatistics',
    desc: '车/人/抛洒/故障事件 今日昨日统计',
});
document.onkeydown = keyDownSearch;
function keyDownSearch(e) {
    const theEvent = e;
    const code = theEvent.keyCode || theEvent.which || theEvent.charCode;
    if (code === 13) {
        const search = document.getElementById('search');
        const searchValue = search.value.toLocaleLowerCase();

        let searchGroup = [];
        for (let i = 0; i < api.length; i++) {

            let apiGroup = api[i];

            let searchArr = [];
            for (let i = 0; i < apiGroup.list.length; i++) {
                let apiData = apiGroup.list[i];
                const desc = apiData.desc;
                if (desc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                    searchArr.push({
                        order: apiData.order,
                        desc: apiData.desc,
                        link: apiData.link,
                        list: apiData.list
                    });
                } else {
                    let methodList = apiData.list || [];
                    let methodListTemp = [];
                    for (let j = 0; j < methodList.length; j++) {
                        const methodData = methodList[j];
                        const methodDesc = methodData.desc;
                        if (methodDesc.toLocaleLowerCase().indexOf(searchValue) > -1) {
                            methodListTemp.push(methodData);
                            break;
                        }
                    }
                    if (methodListTemp.length > 0) {
                        const data = {
                            order: apiData.order,
                            desc: apiData.desc,
                            link: apiData.link,
                            list: methodListTemp
                        };
                        searchArr.push(data);
                    }
                }
            }
            if (apiGroup.name.toLocaleLowerCase().indexOf(searchValue) > -1) {
                searchGroup.push({
                    name: apiGroup.name,
                    order: apiGroup.order,
                    list: searchArr
                });
                continue;
            }
            if (searchArr.length === 0) {
                continue;
            }
            searchGroup.push({
                name: apiGroup.name,
                order: apiGroup.order,
                list: searchArr
            });
        }
        let html;
        if (searchValue === '') {
            const liClass = "";
            const display = "display: none";
            html = buildAccordion(api,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        } else {
            const liClass = "open";
            const display = "display: block";
            html = buildAccordion(searchGroup,liClass,display);
            document.getElementById('accordion').innerHTML = html;
        }
        const Accordion = function (el, multiple) {
            this.el = el || {};
            this.multiple = multiple || false;
            const links = this.el.find('.dd');
            links.on('click', {el: this.el, multiple: this.multiple}, this.dropdown);
        };
        Accordion.prototype.dropdown = function (e) {
            const $el = e.data.el;
            let $this = $(this), $next = $this.next();
            $next.slideToggle();
            $this.parent().toggleClass('open');
            if (!e.data.multiple) {
                $el.find('.submenu').not($next).slideUp("20").parent().removeClass('open');
            }
        };
        new Accordion($('#accordion'), false);
    }
}

function buildAccordion(apiGroups, liClass, display) {
    let html = "";
    if (apiGroups.length > 0) {
        if (apiDocListSize === 1) {
            let apiData = apiGroups[0].list;
            let order = apiGroups[0].order;
            for (let j = 0; j < apiData.length; j++) {
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#_'+order+'_'+apiData[j].order+'_' + apiData[j].link + '">' + apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                html += '<ul class="sectlevel2" style="'+display+'">';
                let doc = apiData[j].list;
                for (let m = 0; m < doc.length; m++) {
                    let spanString;
                    if (doc[m].deprecated === 'true') {
                        spanString='<span class="line-through">';
                    } else {
                        spanString='<span>';
                    }
                    html += '<li><a href="#_'+order+'_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].desc + '">' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
                }
                html += '</ul>';
                html += '</li>';
            }
        } else {
            for (let i = 0; i < apiGroups.length; i++) {
                let apiGroup = apiGroups[i];
                html += '<li class="'+liClass+'">';
                html += '<a class="dd" href="#_'+apiGroup.order+'_' + apiGroup.name + '">' + apiGroup.order + '.&nbsp;' + apiGroup.name + '</a>';
                html += '<ul class="sectlevel1">';

                let apiData = apiGroup.list;
                for (let j = 0; j < apiData.length; j++) {
                    html += '<li class="'+liClass+'">';
                    html += '<a class="dd" href="#_'+apiGroup.order+'_'+ apiData[j].order + '_'+ apiData[j].link + '">' +apiGroup.order+'.'+ apiData[j].order + '.&nbsp;' + apiData[j].desc + '</a>';
                    html += '<ul class="sectlevel2" style="'+display+'">';
                    let doc = apiData[j].list;
                    for (let m = 0; m < doc.length; m++) {
                       let spanString;
                       if (doc[m].deprecated === 'true') {
                           spanString='<span class="line-through">';
                       } else {
                           spanString='<span>';
                       }
                       html += '<li><a href="#_'+apiGroup.order+'_' + apiData[j].order + '_' + doc[m].order + '_' + doc[m].desc + '">'+apiGroup.order+'.' + apiData[j].order + '.' + doc[m].order + '.&nbsp;' + spanString + doc[m].desc + '<span></a> </li>';
                   }
                    html += '</ul>';
                    html += '</li>';
                }

                html += '</ul>';
                html += '</li>';
            }
        }
    }
    return html;
}
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
    desc: '登录',
});
api[0].list[3].list.push({
    order: '2',
    deprecated: 'false',
    url: '/logout',
    desc: '登出',
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
    alias: 'SysMenuController',
    order: '6',
    link: '菜单管理',
    desc: '菜单管理',
    list: []
})
api[0].list[5].list.push({
    order: '1',
    deprecated: 'false',
    url: '/sys/menu/nav',
    desc: 'nav',
});
api[0].list[5].list.push({
    order: '2',
    deprecated: 'false',
    url: '/sys/menu/permissions',
    desc: '当前用户拥有权限',
});
api[0].list[5].list.push({
    order: '3',
    deprecated: 'false',
    url: '/sys/menu/allPermissions',
    desc: '后端所有可配置权限',
});
api[0].list[5].list.push({
    order: '4',
    deprecated: 'false',
    url: '/sys/menu/list',
    desc: '列表',
});
api[0].list[5].list.push({
    order: '5',
    deprecated: 'false',
    url: '/sys/menu/{id}',
    desc: '根据id查询',
});
api[0].list[5].list.push({
    order: '6',
    deprecated: 'false',
    url: '/sys/menu/',
    desc: '保存',
});
api[0].list[5].list.push({
    order: '7',
    deprecated: 'false',
    url: '/sys/menu/',
    desc: '更新',
});
api[0].list[5].list.push({
    order: '8',
    deprecated: 'false',
    url: '/sys/menu/{id}',
    desc: '删除',
});
api[0].list[5].list.push({
    order: '9',
    deprecated: 'false',
    url: '/sys/menu/select',
    desc: '当前用户拥有菜单',
});
api[0].list.push({
    alias: 'SysRoleController',
    order: '7',
    link: '角色管理',
    desc: '角色管理',
    list: []
})
api[0].list[6].list.push({
    order: '1',
    deprecated: 'false',
    url: '/sys/role/page',
    desc: '分页查询',
});
api[0].list[6].list.push({
    order: '2',
    deprecated: 'false',
    url: '/sys/role/list',
    desc: '列表',
});
api[0].list[6].list.push({
    order: '3',
    deprecated: 'false',
    url: '/sys/role/{id}',
    desc: '根据id查询',
});
api[0].list[6].list.push({
    order: '4',
    deprecated: 'false',
    url: '/sys/role/',
    desc: '新增',
});
api[0].list[6].list.push({
    order: '5',
    deprecated: 'false',
    url: '/sys/role/',
    desc: '更新',
});
api[0].list[6].list.push({
    order: '6',
    deprecated: 'false',
    url: '/sys/role/',
    desc: '删除',
});
api[0].list.push({
    alias: 'SysUserController',
    order: '8',
    link: '用户管理',
    desc: '用户管理',
    list: []
})
api[0].list[7].list.push({
    order: '1',
    deprecated: 'false',
    url: '/sys/user/page',
    desc: '分页查询',
});
api[0].list[7].list.push({
    order: '2',
    deprecated: 'false',
    url: '/sys/user/{id}',
    desc: '根据id查询',
});
api[0].list[7].list.push({
    order: '3',
    deprecated: 'false',
    url: '/sys/user/info',
    desc: '当前用户信息',
});
api[0].list[7].list.push({
    order: '4',
    deprecated: 'false',
    url: '/sys/user/password',
    desc: '修改密码',
});
api[0].list[7].list.push({
    order: '5',
    deprecated: 'false',
    url: '/sys/user/',
    desc: '新增',
});
api[0].list[7].list.push({
    order: '6',
    deprecated: 'false',
    url: '/sys/user/',
    desc: '更新',
});
api[0].list[7].list.push({
    order: '7',
    deprecated: 'false',
    url: '/sys/user/',
    desc: '删除',
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
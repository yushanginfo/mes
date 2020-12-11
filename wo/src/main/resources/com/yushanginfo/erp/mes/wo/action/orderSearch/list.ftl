[#ftl]

  <div class="card card-info card-primary card-outline">
    <div class="card-header">
      <h3 class="card-title">工单信息
        <span class="badge badge-primary">${workOrders.totalItems}</span>
      </h3>
      [@b.form name="orderSearchForm" action="!search" class="form-inline ml-3 float-right" ]
        <div class="input-group input-group-sm ">
          <input class="form-control form-control-navbar" type="search" name="q" value="${Parameters['q']!}" aria-label="Search" placeholder="工单单号、图号、品号" autofocus="autofocus">
          [#list Parameters as k,v]
             [#if k != "q"]
             <input name="${k}" value="${v}" type="hidden">
             [/#if]
          [/#list]
          <div class="input-group-append">
            <button class="btn btn-navbar" type="submit" onclick="bg.form.submit(document.orderSearchForm);return false;">
              <i class="fas fa-search"></i>
            </button>
          </div>
        </div>
      [/@]
    </div>
    <div class="card-body">

        <table class="table table-hover table-sm">
          <thead>
             <th>工单单号</th>
             <th>产品图号</th>
             <th>产品名称</th>
             <th>数量</th>
             <th>客户交期</th>
             <th>计划交期</th>
             <th>评审交期</th>
             <th>状态</th>
          </thead>
          <tbody>
          [#list workOrders as order]
           <tr>
            <td>[@b.a href="!info?id=${order.id}" title="${order.orderType.code} ${order.orderType.name}"]${order.batchNum}[/@]</td>
            <td>${order.product.specification!}</td>
            <td>${order.product.name}</td>
            <td>${order.amount}</td>
            <td>${(order.deadline?string("yyyy-MM-dd"))!}</td>
            <td>${(order.plannedEndOn?string("yyyy-MM-dd"))!}</td>
            <td>${(order.scheduledOn?string("yyyy-MM-dd"))!}</td>
            <td>${order.assessStatus.name}</td>
           </tr>
           [/#list]
          </tbody>
         </table>
         <nav aria-label="Page navigation example">
           <ul class="pagination float-right">
             [#if workOrders.pageIndex > 1]
             <li class="page-item"><a class="page-link" href="#" onclick="listOrder(1)">首页</a></li>
             <li class="page-item"><a class="page-link" href="#"  onclick="listOrder(${workOrders.pageIndex-1})">${workOrders.pageIndex-1}</a></li>
             [/#if]
             <li class="page-item active"><a class="page-link" href="#" >${workOrders.pageIndex}</a></li>
             [#if workOrders.pageIndex < workOrders.totalPages]
             <li class="page-item"><a class="page-link" href="#" onclick="listOrder(${workOrders.pageIndex+1})">${workOrders.pageIndex+1}</a></li>
             <li class="page-item"><a class="page-link" href="#" onclick="listOrder(${workOrders.totalPages})">末页</a></li>
             [/#if]
           </ul>
         </nav>
    </div>
  </div>
  <script>
     var qElem = document.orderSearchForm['q'];
     qElem.focus();
     if(qElem.setSelectionRange && qElem.value.length>0){
       qElem.setSelectionRange(qElem.value.length,qElem.value.length)
     }
     function listOrder(pageIndex){
        bg.form.addInput(document.orderSearchForm,"pageIndex",pageIndex);
        bg.form.submit(document.orderSearchForm);
     }
  </script>

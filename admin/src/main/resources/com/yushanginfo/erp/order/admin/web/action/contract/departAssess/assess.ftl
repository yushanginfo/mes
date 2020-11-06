[#ftl]
[@b.head/]
  [#assign assessMap={}]
  [#list salesOrder.assesses as assess]
    [#assign assessMap=assessMap+{assess.technic.id?string:assess}]
  [/#list]

[@b.toolbar title=depart.name+  " 订单评审"]bar.addBack();[/@]
  [@b.form action="!saveAssess" theme="list"]
    [@b.field label="订单编号"]${salesOrder.code}[/@]
    [@b.field label="生产批号"]${salesOrder.batchNum}[/@]
    [@b.field label="产品图号"]${salesOrder.product.specification!}[/@]
    [@b.field label="订单数量"]${salesOrder.amount}[/@]
    [@b.field label="客户交期"]${salesOrder.deadline?string("yyyy-MM-dd")}[/@]
    [@b.field label="计划交期"]${(salesOrder.plannedEndOn?string("yyyy-MM-dd"))}[/@]
    [@b.field label="到料日期"]${(salesOrder.materialDate?string("yyyy-MM-dd" ))!}[/@]
    [@b.field label="订单备注"]${(salesOrder.remark)?default("无")}[/@]

    [#list salesOrder.technicScheme.technics as pt]
    [#if depart.id==pt.technic.depart.id]
    [#assign technic=pt.technic/]
    [@b.field label=pt.technic.name]
        <select name="technic_${technic.id}.factory.id" style="width:80px">
          [#if (assessMap[technic.id?string].factory)??]
            [#assign assessFactory = assessMap[technic.id?string].factory/]
          [#else]
            [#assign assessFactory= salesOrder.factory/]
          [/#if]
          [#list factories as f]
            <option value="${f.id}" [#if f.id==assessFactory.id]selected="selected"[/#if]>${f.name}</option>
          [/#list]
        </select>
        <input name="technic_${technic.id}.days" style="width:80px" type="number" value="${(assessMap[technic.id?string].days)!}"/>天
        <label class="comment">${pt.description!}
               [#if pt.machine??]${pt.machine.code} ${pt.machine.name}
               [#elseif pt.supplier??] ${pt.supplier.code} ${pt.supplier.name}
               [/#if]
        </label>
    [/@]
    [/#if]
    [/#list]
    [@b.formfoot]
    <input type="hidden" name="salesOrderId" value="${salesOrder.id}"/>
    <input type="hidden" name="departId" value="${depart.id}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
  [/@]
  <script>
     function checkDays(form){

     }
  </script>
[@b.foot/]

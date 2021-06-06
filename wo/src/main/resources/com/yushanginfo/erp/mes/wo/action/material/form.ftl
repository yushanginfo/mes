[#ftl]
[@b.head/]
[@b.toolbar title="编辑到料日期"]bar.addBack();[/@]
  [@b.form action=b.rest.save(workOrder) theme="list"]
    [@b.field label="工单单号"]${workOrder.orderType.code} ${workOrder.batchNum}[/@]
    [@b.field label="产品图号"]${workOrder.product.specification!}[/@]
    [@b.field label="计划数量"]${workOrder.amount}[/@]
    [@b.field label="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!"未设置"}[/@]
    [@b.field label="材料清单"]
      <div style="margin-left:100px">
      [#list workOrder.product.bom as i]${i.material.name} ${i.material.specification!} [#if i.amount??]${i.amount*workOrder.amount} ${i.material.unit.name}[#else]??[/#if][#if i_has_next]<br>[/#if][/#list]
      </div>
    [/@]
    [@b.field label="备注"]${(workOrder.remark)?default("无")}[/@]
    [@b.radios id="ready" label="是否有料" name="materialAssess.ready" value=materialAssess.ready! onclick="displayReadyOn(this)"/]
    [#assign dataRange]建议:${b.now?string('yyyy-MM-dd')}~${(workOrder.deadline?string('yyyy-MM-dd'))!"--"}[/#assign]
    [#-- maxDate="${workOrder.deadline?string('yyyy-MM-dd')}"--]
    [@b.date id="readyOn" name="materialAssess.readyOn" label="到料日期" value=(materialAssess.readyOn)! minDate="${b.now?string('yyyy-MM-dd')}" format="yyyy-MM-dd" comment=dataRange/]
    [#if !workOrder.status.name?contains('完工') && workOrder.assessStatus!="通过"]
    [@b.formfoot]
      <input type="hidden" name="materialAssess.id" value="${materialAssess.id!}"/>
      [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
    [/@]
    [/#if]
  [/@]
  <script>
    jQuery(function() {
        [#if materialAssess.ready]
        displayReadyOn(document.getElementById("ready_1"));
        [#else]
        displayReadyOn(document.getElementById("ready_0"));
        [/#if]
    })
  function displayReadyOn(ele){
        var hidden=ele.checked && jQuery(ele).val()=='1';
        for(var i=8;i<=8;i++){
          if(hidden){
            jQuery(ele).parentsUntil("ol").parent().children("li:nth("+i+")").hide();
            jQuery("#readyOn").val("");
          }else{
            jQuery(ele).parentsUntil("ol").parent().children("li:nth("+i+")").show();
          }
        }
      }
  </script>
[@b.foot/]

[#ftl]
[@b.head/]
[@b.toolbar title="发起复审"]bar.addBack();[/@]
  [@b.form action="!issueReview" theme="list"]
    [@b.field label="工单单号"]${workOrder.orderType.code} ${workOrder.batchNum}[/@]
    [@b.field label="产品图号"]${workOrder.product.specification!}[/@]
    [@b.field label="计划数量"]${workOrder.amount}[/@]
    [@b.field label="客户交期"]${(workOrder.deadline?string("yyyy-MM-dd"))!"未设置"}[/@]
    [@b.field label="计划交期"]${(workOrder.plannedEndOn?string("yyyy-MM-dd"))!"未设置"}[/@]
    [@b.field label="评审交期"]${(workOrder.scheduledOn?string("yyyy-MM-dd"))!"未设置"}[/@]
    [@b.field label="材料清单"]
      [#list workOrder.product.bom as i]${i.material.name} ${i.material.specification!} [#if i.amount??]${i.amount*workOrder.amount} ${i.material.unit.name}[#else]??[/#if][#if i_has_next]<br>[/#if][/#list]
    [/@]
    [@b.field label="到料信息"]
      [#if workOrder.materialAssess??] [#if workOrder.materialAssess.ready]有料[#else] ${(workOrder.materialAssess.readyOn?string("yyyy-MM-dd"))!}[/#if] <span style="font-size:0.8rem;color: #999;">${(workOrder.materialAssess.assessedBy.name)!} ${(workOrder.materialAssess.createdAt)?string("yyyy-MM-dd HH:mm")}</span>[/#if]
    [/@]
    [@b.field label="评审信息"]
      <div style="margin-left:110px">
      [#list workOrder.technics?sort_by("indexno") as wt]
         ${wt.indexno} ${wt.technic.name}(${wt.technic.description!})
         [#if wt.days??]
         ${wt.factory.name} ${wt.days}天 <span style="font-size:0.8rem;color: #999;">${(wt.assessedBy.name)!} ${wt.updatedAt?string("yyyy-MM-dd HH:mm")} [#if !wt.passed]需要复审[/#if]</span>
         [#else]尚未评审[/#if] [#if wt_has_next]<br>[/#if]
      [/#list]
      [#if workOrder.technics?size==0]无[/#if]
      </div>
    [/@]
    [@b.field label="备注"]${(workOrder.remark)?default("无")}[/@]
    [#if workOrder.assessStatus.name='待复审' && workOrder.status!="通过"]
      [@b.textfield name="reviewEvent.comments"  label="复审原因" maxlength="200" style="width:300px" required="true"/]
      [@b.textarea name="reviewEvent.remark" maxlength="300"  label="其他说明" style="width:300px" rows="3" cols="80"/]
      [@b.select name="watcherIds" label="结果反馈"  style="width:300px;"  option="id,name" empty="..." href=ems.base +"/mes/base/users.json?q={term}"/]
      [@b.formfoot]
        <input type="hidden" name="workOrder.id" value="${workOrder.id!}"/>
        [@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
      [/@]
    [#else]
      [#if workOrder.reviewEvents?size>0]
       [#assign reviewEvent=workOrder.reviewEvents?sort_by("updatedAt")?reverse?first/]
       [@b.field label="复审原因"]${reviewEvent.comments}[/@]
       [@b.field label="其他说明"]${reviewEvent.remark!}[/@]
       [@b.field label="结果反馈"][#list reviewEvent.watchers as w]${w.name}&nbsp;[/#list]&nbsp;[/@]
      [#else]
        [@b.field label="其他说明"]<span style="color:red">不能进行复审，或没有复审数据.</span>[/@]
      [/#if]
    [/#if]
  [/@]
  <script>
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

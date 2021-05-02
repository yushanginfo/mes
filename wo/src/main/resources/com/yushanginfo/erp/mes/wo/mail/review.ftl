<!DOCTYPE html>
<html lang="zh_CN">
  <head>
    <title></title>
    <meta http-equiv="content-type" content="text/html;charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta http-equiv="pragma" content="no-cache"/>
    <meta http-equiv="cache-control" content="no-cache"/>
  </head>
  <body>
  <div style="width: 100%;padding-right: 15px;padding-left: 15px;margin-right: auto;margin-left: auto;max-width: 1140px;box-sizing: border-box;">
<div style="position: relative;display: flex;flex-direction: column;min-width: 0;/*! word-wrap: break-word; */background-color: #fff;background-clip: border-box;border: 1px solid rgba(0,0,0,.125);border-radius: .25rem;box-sizing: border-box;">
  <div style="flex: 1 1 auto;min-height: 1px;padding: 1.25rem;">
    <h5 style="margin-bottom: .75rem;font-size: 1.25rem;font-weight: 500;line-height: 1.2;margin-top: 0;">工单${workOrder.batchNum} ${reviewEvent.issueBy.name}发起${workOrder.reviewEvents?size}轮复审</h5>
      <table class="infoTable">
        <tr>
          <td class="title"  width="20%">工单单别</td>
          <td class="content">${workOrder.orderType.name}</td>
          <td class="title"  width="20%">工单单号</td>
          <td class="content">${workOrder.batchNum}</td>
        </tr>
        <tr>
          <td class="title">品号</td>
          <td class="content">${(workOrder.product.code)!}</td>
          <td class="title">产品图号</td>
          <td class="content">${(workOrder.product.specification)!}</td>
        </tr>
        <tr>
          <td class="title">工单数量</td>
          <td class="content">${workOrder.amount!}</td>
          <td class="title">所在厂区</td>
          <td class="content">${workOrder.factory.name}</td>
        </tr>
        <tr>
          <td class="title">工单状态</td>
          <td class="content">${(workOrder.status.name)!}</td>
          <td class="title">评审状态</td>
          <td class="content">${workOrder.assessStatus.name!}</td>
        </tr>
        <tr>
          <td class="title">客户交期</td>
          <td class="content">${(workOrder.deadline?string("yyyy-MM-dd" ))!}</td>
          <td class="title">计划交期</td>
          <td class="content">${(workOrder.plannedEndOn?string("yyyy-MM-dd"))!}</td>
        </tr>
        <tr>
          <td class="title">到料日期</td>
          <td class="content">[#if workOrder.materialAssess??] [#if workOrder.materialAssess.ready]有料[#else] ${(workOrder.materialAssess.readyOn?string("yyyy-MM-dd"))!}[/#if] <span style="font-size:0.8rem;color: #999;">${(workOrder.materialAssess.assessedBy.name)!} ${(workOrder.materialAssess.createdAt)?string("yyyy-MM-dd HH:mm")}</span>[/#if]</td>
          <td class="title">评审交期</td>
          <td class="content">${(workOrder.scheduledOn?string("yyyy-MM-dd" ))!}</td>
        </tr>
        <tr>
          <td class="title">材料清单</td>
          <td class="content" colspan="3">
            [#list workOrder.product.bom as i]${i.material.name} ${i.material.specification!} [#if i.amount??]${i.amount*workOrder.amount} ${i.material.unit.name}[#else]??[/#if][#if i_has_next]<br>[/#if][/#list]
          </td>
        </tr>
        <tr>
          <td class="title">工艺列表</td>
          <td class="content"  colspan="3">
             [#list workOrder.technics as wt]<span title="${(wt.technic.assessGroup.name)!'--'}">${wt.technic.name}</span>[#if wt_has_next],[/#if][/#list]
          </td>
        </tr>
        <tr>
          <td class="title">评审信息</td>
          <td class="content" colspan="3">
            [#list workOrder.technics?sort_by("indexno") as wt]
               ${wt.indexno} ${wt.technic.name}(${wt.technic.description!})
               [#if wt.days??]
               ${wt.factory.name} ${wt.days}天 <span style="font-size:0.8rem;color: #999;">${(wt.assessedBy.name)!} ${wt.updatedAt?string("yyyy-MM-dd HH:mm")}</span>
               [#else]尚未评审[/#if] [#if wt_has_next]<br>[/#if]
            [/#list]
          </td>
        </tr>
        <tr>
          <td class="title">复审原因</td>
          <td class="content" colspan="3">
          ${reviewEvent.comments!}
          [#if reviewEvent.remark??]
          <p>
            ${reviewEvent.remark!}
          </p>
          [/#if]
          </td>
        </tr>
      </table>
    </div>
  </div>

 </div> <!--container-->
  </body>
</html>

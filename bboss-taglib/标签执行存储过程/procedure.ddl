CREATE PROCEDURE P_JHJD_GCJHData 
@HTBH varchar(12), 
@StartDate varchar(6), 
@EndDate varchar(6) 
AS--工程计划
set nocount onCreate Table #Ls_JiHua 
(  桩号 varchar(20),  
细目编号 varchar(6),  
细目名称 varchar(30),  
单位 varchar(10),  
完成数量1 money,  
完成数量2 money,  
完成金额 money,  
日期  varchar(6),  
ZH varchar(16))if @HTBH=''
begin
/*SELECT 
dbo.JHJD_GCJH.BDBH AS 合同编号, 
dbo.JHJD_GCJH.XMBH AS 细目编号,       
dbo.JHJD_XMQD.XMMC AS 细目名称, 
dbo.JHJD_XMQD.DW AS 单位,       
dbo.JHJD_GCJH.ZH AS 桩号, 
dbo.JHJD_GCJH.RQ AS 日期,       
dbo.JHJD_GCJH.WC AS 完成数量, 
dbo.JHJD_GCJH.WC1 AS 完成数量1,       
dbo.JHJD_GCJH.WCJE AS 完成金额
FROM dbo.JHJD_XMQD INNER JOIN      
dbo.JHJD_GCJH ON 
dbo.JHJD_XMQD.XMBH = dbo.JHJD_GCJH.XMBH
Where  (dbo.JHJD_GCJH.RQ>=@StartDate) 
and(dbo.JHJD_GCJH.RQ<=@endDate)
ORDER BY 合同编号, 日期, 
LEFT(dbo.JHJD_GCJH.XMBH, 4),
 桩号, 
 RIGHT(dbo.JHJD_GCJH.XMBH, 2)
 */
 SELECT dbo.JHJD_GCJH.BDBH AS 合同编号, 
 dbo.JHJD_GCJH.XMBH AS 细目编号,       
 dbo.JHJD_XMQD.XMMC AS 细目名称, 
 dbo.JHJD_XMQD.DW AS 单位,       
 dbo.JHJD_GCJH.ZH AS 桩号, 
 dbo.JHJD_GCJH.RQ AS 日期,       
 dbo.JHJD_GCJH.WC AS 完成数量1, 
 dbo.JHJD_GCJH.WC1 AS 完成数量2,       
 dbo.JHJD_GCJH.WCJE AS 完成金额
 FROM dbo.JHJD_XMQD INNER JOIN      
 dbo.JHJD_GCJH ON dbo.JHJD_XMQD.XMBH = dbo.JHJD_GCJH.XMBHWhere  
 (dbo.JHJD_GCJH.RQ>=@StartDate) 
 and(dbo.JHJD_GCJH.RQ<=@endDate)
 ORDER BY 合同编号, 日期, LEFT(dbo.JHJD_GCJH.XMBH, 4), 桩号, RIGHT(dbo.JHJD_GCJH.XMBH, 2)endelsebegindeclare @StartYear intdeclare @StartMonth intdeclare @EndYear intdeclare @EndMonth intset @StartYear=cast(Left(@StartDate,4) as int)set @StartMonth=Cast(Right(@StartDate,2) as Int)set @EndYear=Cast(Left(@EndDate,4) as int)Set @EndMonth=Cast(Right(@EndDate,2) as Int)declare @RQ varchar(6)while (@StartYear*100+@StartMonth<=@EndYear*100+@EndMonth)begin  Set @RQ=Cast(@StartYear as Char(4))+Right('0'+Cast(@StartMonth as VarChar(2)),2)  insert into #Ls_JiHua (    细目名称  ) values (Left(@RQ,4)+'年'+Right(@RQ,2)+'月   路基工程')   insert into #Ls_JiHua (  细目编号 ,  细目名称 , 单位, 日期 )              select XMBH,XMMC,DW,@RQ from JHJD_XMQD where XMBH like 'LJ%' order by XMBH  insert into #Ls_JiHua (    细目名称  ) values (Left(@RQ,4)+'年'+Right(@RQ,2)+'月   路面工程')   insert into #Ls_JiHua (  细目编号 ,  细目名称 , 单位,  日期 )             select XMBH,XMMC,DW,@RQ from JHJD_XMQD where XMBH like 'LM%' order by XMBH insert into #Ls_JiHua (    细目名称  ) values (Left(@RQ,4)+'年'+Right(@RQ,2)+'月   桥涵工程')  insert into #Ls_JiHua (  细目编号 ,  细目名称 ,  单位, 日期,ZH,桩号 )  select bbxx.XMBH,bbxx.XMMC,bbxx.DW,@RQ,bbxx.ZHBH,bbxx.zhmc from   (select JHJD_XMQD.*,zh.mc as zhmc,isnull(zh.zhbh,' ')as zhbh from JHJD_XMQD left join      (select mc,ssxmbh,bh as zhbh from JHJD_zh where bh like @HTBH+'%') AS zh on substring(JHJD_XMQD.XMbh,1,4)=substring(zh.ssxmbh,1,4) and JHJD_XMQD.XMbh<>zh.ssxmbh     where JHJD_XMQD.XMbh like 'QH%'and substring(JHJD_XMQD.XMBH,3,4)<>'0000') bbxx   order by substring(bbxx.xmbh,1,4),(case when left(bbxx.zhmc,1)<>'k'then 9999999 else (case when charindex('+',bbxx.zhmc)=0 then right(bbxx.zhmc,len(bbxx.zhmc)-1)*1000 else cast(substring(bbxx.zhmc,2,charindex('+',bbxx.zhmc)-2)*1000 as float)+substring(bbxx.zhmc,charindex('+',bbxx.zhmc)+1,len(bbxx.zhmc)-charindex('+',bbxx.zhmc)) end) end),bbxx.zhmc,substring(bbxx.xmbh,3,4) insert into #Ls_JiHua (    细目名称  ) values (Left(@RQ,4)+'年'+Right(@RQ,2)+'月   交叉工程')  insert into #Ls_JiHua (  细目编号 ,  细目名称 ,  单位, 日期,ZH,桩号 )  select bbxx.XMBH,bbxx.XMMC,bbxx.DW,@RQ,bbxx.ZHBH,bbxx.zhmc from   (select JHJD_XMQD.*,zh.mc as zhmc,isnull(zh.zhbh,' ')as zhbh from JHJD_XMQD left join      (select mc,ssxmbh,bh as zhbh from JHJD_zh where bh like @HTBH+'%') AS zh on substring(JHJD_XMQD.XMbh,1,4)=substring(zh.ssxmbh,1,4) and JHJD_XMQD.XMbh<>zh.ssxmbh     where JHJD_XMQD.XMbh like 'JC%'and substring(JHJD_XMQD.XMBH,3,4)<>'0000') bbxx   order by substring(bbxx.xmbh,1,4),(case when left(bbxx.zhmc,1)<>'k'then 9999999 else (case when charindex('+',bbxx.zhmc)=0 then right(bbxx.zhmc,len(bbxx.zhmc)-1)*1000 else cast(substring(bbxx.zhmc,2,charindex('+',bbxx.zhmc)-2)*1000 as float)+substring(bbxx.zhmc,charindex('+',bbxx.zhmc)+1,len(bbxx.zhmc)-charindex('+',bbxx.zhmc)) end) end),bbxx.zhmc,substring(bbxx.xmbh,3,4) Set @StartMonth=@StartMonth+1 if @StartMonth>12 begin  set  @StartYear=@StartYear+1  Set @StartMonth=1 endend--Update #Ls_JiHua set  完成数量=a.WC,完成数量1=a.WC1,完成金额=a.WCJE from JHJD_GCJH  a where  a.XMBH=细目编号 and a.RQ=日期 and a. ZH=#Ls_JiHua.ZHUpdate #Ls_JiHua set  完成数量1=a.WC,完成数量2=a.WC1,完成金额=a.WCJE from JHJD_GCJH  a where a.BDBH=@HTBH and  a.XMBH=细目编号  and a.RQ=日期 and  #Ls_JiHua.细目编号 like 'LJ%'Update #Ls_JiHua set  完成数量1=a.WC,完成数量2=a.WC1,完成金额=a.WCJE from JHJD_GCJH  a where  a.BDBH=@HTBH and a.XMBH=细目编号  and a.RQ=日期 and  #Ls_JiHua.细目编号 like 'LM%'Update #Ls_JiHua set  完成数量1=a.WC,完成数量2=a.WC1,完成金额=a.WCJE from JHJD_GCJH  a where a.BDBH=@HTBH and  a.XMBH=细目编号  and a.RQ=日期 and a. ZH=#Ls_JiHua.ZH and #Ls_JiHua.细目编号 like 'QH%'Update #Ls_JiHua set  完成数量1=a.WC,完成数量2=a.WC1,完成金额=a.WCJE from JHJD_GCJH  a where a.BDBH=@HTBH and  a.XMBH=细目编号  and a.RQ=日期 and a. ZH=#Ls_JiHua.ZH and #Ls_JiHua.细目编号 like 'JC%'/*SELECT dbo.JHJD_GCJH.BDBH AS 合同编号, dbo.JHJD_GCJH.XMBH AS 细目编号,       dbo.JHJD_XMQD.XMMC AS 细目名称, dbo.JHJD_XMQD.DW AS 单位,       dbo.JHJD_GCJH.ZH AS 桩号, dbo.JHJD_GCJH.RQ AS 日期,       dbo.JHJD_GCJH.WC AS 完成数量, dbo.JHJD_GCJH.WC1 AS 完成数量1,       dbo.JHJD_GCJH.WCJE AS 完成金额FROM dbo.JHJD_XMQD INNER JOIN      dbo.JHJD_GCJH ON dbo.JHJD_XMQD.XMBH = dbo.JHJD_GCJH.XMBHWhere (dbo.JHJD_GCJH.BDBH=@HTBH) and (dbo.JHJD_GCJH.RQ>=@StartDate) and(dbo.JHJD_GCJH.RQ<=@endDate)ORDER BY 合同编号, 日期,  LEFT(dbo.JHJD_GCJH.XMBH, 4), 桩号, RIGHT(dbo.JHJD_GCJH.XMBH, 2)*/end/*select   桩号 as zh,  细目编号 as xmbh ,  细目名称 as xmmc,  单位 as dw,  完成数量1 as sl1 ,   完成数量2 as sl2,  完成金额 as je,  日期  as rq  from #Ls_JiHua*/select   桩号 ,  细目编号  ,  细目名称 ,  单位 ,  完成数量1  ,   完成数量2 ,  完成金额 ,  日期    from #Ls_JiHuaGO
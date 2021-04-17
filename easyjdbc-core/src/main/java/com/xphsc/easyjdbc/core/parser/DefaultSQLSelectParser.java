/*
 * Copyright (c) 2018-2019  huipei.x
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xphsc.easyjdbc.core.parser;
import com.xphsc.easyjdbc.core.SimpleJdbcDao;
import com.xphsc.easyjdbc.page.PageRowBounds;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.page.PageInfoImpl;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.Collects;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public class DefaultSQLSelectParser implements SQLSelectParser  {
    protected static PageInfo pageInfo;
    protected static Class<?> entityClass;
    @Override
    public  Object select(String selectSql, SimpleJdbcDao simpleJdbcDao, Class<?> modelClass, Method method, Map<String, Object> paramsMap){
        MethodReturnType methodReturnType=new MethodReturnType(method, modelClass);
        SQLParser sqlParser=new DefaultSQLParser();
        Object returnResult=null;
        if(methodReturnType.returnsList){
            Object[] result=null;
            if(sqlParser.hasFieldPlaceHolder(selectSql)||sqlParser.hasOgnlPlaceHolder(selectSql)||sqlParser.hasObjectPlaceHolder(selectSql)){
                if(sqlParser.hasFieldPlaceHolder(selectSql)){
                    result =sqlParser.sqlPlaceHolder(selectSql, paramsMap, false);
                }
                if (sqlParser.hasOgnlPlaceHolder(selectSql)){
                    result =sqlParser.sqlPlaceHolder(selectSql, paramsMap, true);
                }
                if (sqlParser.hasObjectPlaceHolder(selectSql)){
                    result =sqlParser.sqlPlaceHolder(selectSql, paramsMap,false);
                }
                if(Collects.isNotEmpty(paramsMap)){
                    returnResult= methodReturnType.returnsMap?
                            simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().queryForList((String) result[0], (Object[]) result[1]):
                            simpleJdbcDao.getEasyJdbcTemplate().find((String) result[0], modelClass, (Object[]) result[1]);
                }
            }else{
                    returnResult= methodReturnType.returnsMap ?
                            simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().queryForList(selectSql) :
                            simpleJdbcDao.getEasyJdbcTemplate().find(selectSql, modelClass, null);
            }
            if(methodReturnType.returnsOptional){
                return  Optional.ofNullable(returnResult);
            }else {
                return returnResult;
            }


        }
        if(methodReturnType.returnsPage){

            Object[] result=null ;
            if(sqlParser.hasFieldPlaceHolder(selectSql)){
                result =sqlParser.sqlPlaceHolder(selectSql, paramsMap, false);
            }
            if (sqlParser.hasOgnlPlaceHolder(selectSql)){
                result =sqlParser.sqlPlaceHolder(selectSql, paramsMap, true);
            }
            if (sqlParser.hasObjectPlaceHolder(selectSql)){
                result =sqlParser.sqlPlaceHolder(selectSql, paramsMap,false);
            }
            if(pageInfo==null){
                pageInfo=new PageInfo();
            }
            boolean containsOffset=Collects.containsKey(paramsMap, "offset");
            boolean containsLimit=Collects.containsKey(paramsMap,"limit");
            if(!containsOffset&&!containsLimit){
                    Assert.isTrue(Collects.containsKey(paramsMap, "pageNum"), "The parameter must have pageNum attribute");
                    Assert.isTrue(Collects.containsKey(paramsMap, "pageSize"), "The parameter must have pageSize attribute");
                    Integer pageNum= Collects.getInteger( paramsMap, "pageNum");
                    Integer pageSize=Collects.getInteger(paramsMap,"pageSize");
                    pageInfo.setPageNum(pageNum);
                    pageInfo.setPageSize(pageSize);

            }else{
                Assert.isTrue(containsOffset,"Parameters must have offset attributes");
                Assert.isTrue(containsLimit, "The parameter must have a limit attribute");
                Integer offset= Collects.getInteger( paramsMap, "offset");
                Integer limit=Collects.getInteger( paramsMap,"limit");
                pageInfo.setOffset(offset);
                pageInfo.setPageSize(limit);

            }
            PageInfo<?> resultPage = null;
            if(result==null){
                Object[]  sqlAndParams={selectSql,null};
                if(!containsOffset&&!containsLimit&&!methodReturnType.returnsMap){
                    resultPage=  simpleJdbcDao.getEasyJdbcTemplate().findByPage((String) sqlAndParams[0], modelClass, pageInfo,null);
                }
                if(containsOffset&&containsLimit&&!methodReturnType.returnsMap){
                    resultPage=simpleJdbcDao.getEasyJdbcTemplate().findByPage((String) sqlAndParams[0], modelClass, pageInfo.getOffset(),pageInfo.getPageSize(), null);
                }

                returnResult= methodReturnType.returnsMap?selectByPageInfo(simpleJdbcDao,sqlAndParams,pageInfo,paramsMap):
                        resultPage;

            }else{
                if(!containsOffset&&!containsLimit&&!methodReturnType.returnsMap){
                    resultPage=simpleJdbcDao.getEasyJdbcTemplate().findByPage((String) result[0], modelClass, pageInfo,(Object[]) result[1]);
                } if(containsOffset&&containsLimit&&!methodReturnType.returnsMap){
                    resultPage= simpleJdbcDao.getEasyJdbcTemplate().findByPage((String) result[0], modelClass, pageInfo.getOffset(),pageInfo.getPageSize(),(Object[]) result[1]);
                }
                returnResult= methodReturnType.returnsMap?selectByPageInfo(simpleJdbcDao,result,pageInfo,paramsMap):
                        resultPage;

            }
            if(methodReturnType.returnsOptional){
                return  Optional.ofNullable(returnResult);
            }else {
                return returnResult;
            }
        }
        else{
            Object[] result=null;
            if(sqlParser.hasFieldPlaceHolder(selectSql)){
                result =sqlParser.sqlPlaceHolder(selectSql, paramsMap, false);
            }
            if (sqlParser.hasOgnlPlaceHolder(selectSql)){
                result =sqlParser.sqlPlaceHolder(selectSql, paramsMap,true);
            }
            if (sqlParser.hasObjectPlaceHolder(selectSql)){
                result =sqlParser.sqlPlaceHolder(selectSql, paramsMap,false);
            }
            Object[] sqlAndParams={selectSql,null};
            if(methodReturnType.returnsCountTypes){
                returnResult= result==null?count(simpleJdbcDao, method.getReturnType(),sqlAndParams) : count(simpleJdbcDao, method.getReturnType(), result);
            } else if(methodReturnType.returnsVoid){
                  simpleJdbcDao.getEasyJdbcTemplate().execute((String) sqlAndParams[0]);
            } else if(!methodReturnType.returnsVoid){
                result=result==null?sqlAndParams:result;
                returnResult= methodReturnType.returnsMap?
                       simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().queryForMap((String) result[0], (Object[]) result[1]):
                        simpleJdbcDao.getEasyJdbcTemplate().get((String) result[0], modelClass, (Object[]) result[1]);

            }
            if(methodReturnType.returnsOptional){
                return  Optional.ofNullable(returnResult);
            }else {
                return returnResult;
            }

        }

    }

    private static PageInfo<Map<String,Object>> selectByPageInfo(SimpleJdbcDao simpleJdbcDao, Object[] result,PageInfo pageInfo, Map<String, Object> paramsMap){
        long total=0L;
        List<Map<String,Object>> maps=null;
        String sql="";
        if(!Collects.containsKey(paramsMap,"offset")&&!Collects.containsKey(paramsMap,"limit")){
            Assert.isTrue(pageInfo.getPageNum() >= 1, "PageNum must be greater than or equal to 1");
            Assert.isTrue(pageInfo.getPageSize() > 0, "PageSize must be greater than 0");
            sql = PageRowBounds.pagination(simpleJdbcDao.getEasyJdbcTemplate().getDialectName(), (String) result[0], (pageInfo.getPageNum() - 1) * pageInfo.getPageSize(), pageInfo.getPageSize());
        }else{
            Integer offset= Collects.getInteger( paramsMap, "offset");
            Integer limit=Collects.getInteger( paramsMap,"limit");
            sql = PageRowBounds.pagination(simpleJdbcDao.getEasyJdbcTemplate().getDialectName(), (String) result[0], offset, limit);
        }
        if(result[1]==null){
            total=simpleJdbcDao.getEasyJdbcTemplate().count((String) result[0]);
            maps=simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().queryForList(sql);
        }else{
            total=simpleJdbcDao.getEasyJdbcTemplate().count((String) result[0],(Object[]) result[1]);
            maps=simpleJdbcDao.getEasyJdbcTemplate().getJdbcBuilder().queryForList(sql, (Object[]) result[1]);
        }
        return  new PageInfoImpl<>(maps,total,pageInfo.getPageNum(),pageInfo.getPageSize());
    }

    private static Object count(SimpleJdbcDao simpleJdbcDao,Class<?> returnType, Object[] result){
        Object count= simpleJdbcDao.getEasyJdbcTemplate().count((String) result[0], (Object[]) result[1]);
        Object IntegerResult=
                returnType.isAssignableFrom(int.class)||
            returnType.isAssignableFrom(Integer.class)?Integer.valueOf(count.toString()):null;
        Object longResult=
                returnType.isAssignableFrom(long.class)||
                        returnType.isAssignableFrom(Long.class)?Long.valueOf(count.toString()):null;
        return IntegerResult==null?longResult:IntegerResult;
    }


  public static class MethodReturnType{
      private  boolean returnsCountTypes;
      private  boolean returnsMap;
      private  boolean returnsPage;
      private  boolean returnsList;
      private  boolean returnsVoid;
      private  boolean returnsOptional;
      private static Set<Class<? extends Object>> returnCountTypes = new HashSet<Class<? extends Object>>();
      static {
          returnCountTypes.add(Number.class);
          returnCountTypes.add(Integer.class);
          returnCountTypes.add(int.class);
          returnCountTypes.add(long.class);
          returnCountTypes.add(Long.class);

      }

      public MethodReturnType(Method method, Class<?> modelClass){
          Class<?> returnType = method.getReturnType();
          Type type = method.getGenericReturnType();
          if(getReturnCountTypes(method)!=null){
              this.returnsCountTypes=true;
          }
          if(returnType.isAssignableFrom(Map.class)){
              this.returnsMap=true;
          }
          if(returnType.isAssignableFrom(PageInfo.class)){
              this.returnsPage=true;
          }

          if(returnType.isAssignableFrom(List.class)){
              this.returnsList=true;
          }

          if(returnType.isAssignableFrom(void.class)){
              this.returnsVoid=true;
          }
          this.returnsOptional = Optional.class.equals(returnType);

          if(type instanceof ParameterizedType){
              final String EQURE_MAP="Map";
                if(returnsOptional){
                    type = ((ParameterizedType) type).getActualTypeArguments()[0];
                    if(type.equals(Map.class)||
                        type.equals(Number.class)||
                        type.equals(Integer.class)||
                        type.equals(int.class)||
                        type.equals(long.class)||
                        type.equals(Long.class)||
                        type.equals(modelClass)){
                        entityClass= (Class<?>) type;
                    }else{
                        this.returnsList=((ParameterizedType) type).getRawType().equals(List.class);
                        this.returnsPage=((ParameterizedType) type).getRawType().equals(PageInfo.class);
                        if (((ParameterizedType) type).getActualTypeArguments()[0].getTypeName().contains(EQURE_MAP)){
                            entityClass=Map.class;
                        }else{
                            entityClass = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                        }
                    }


                }else{
                     if (((ParameterizedType) type).getActualTypeArguments()[0].getTypeName().contains(EQURE_MAP)){
                        entityClass=Map.class;
                    }else{
                        entityClass = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                    }



                }

          }

          if(entityClass!=null){
              if(entityClass.isAssignableFrom(Map.class)){
                  returnsMap=true;
              }

              if(     entityClass.isAssignableFrom(Number.class)||
                      entityClass.isAssignableFrom(Integer.class)||
                      entityClass.isAssignableFrom(int.class)||
                      entityClass.isAssignableFrom(long.class)||
                      entityClass.isAssignableFrom(Long.class)
              ){
                  returnsCountTypes=true;
              }
          }
          if(modelClass!=null){
              if(modelClass.isAssignableFrom(Map.class)){
                  returnsMap=true;
              }
          }


      }


      private Class<? extends Object> getReturnCountTypes(Method method) {
          return chooseReturnType(method, returnCountTypes);
      }

      private Class<? extends Object> chooseReturnType(Method method, Set<Class<? extends Object>> types) {

          for (Class<? extends Object> type : types) {
              if(type.equals(method.getReturnType())){
                  return type;
              }

          }

          return null;
      }


  }



}

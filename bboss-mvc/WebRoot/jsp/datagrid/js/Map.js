/*  
 * 接口： size() 获取MAP元素个数 isEmpty() 判断MAP是否为空 clear() 删除MAP所有元素 put(key, value)  
 * 向MAP中增加元素（key, value) remove(key) 删除指定KEY的元素，成功返回True，失败返回False get(key)  
 * 获取指定KEY的元素值VALUE，失败返回NULL element(index)  
 * 获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL containsKey(key)  
 * 判断MAP中是否含有指定KEY的元素 containsValue(value) 判断MAP中是否含有指定VALUE的元素 values()  
 * 获取MAP中所有VALUE的数组（ARRAY） keys() 获取MAP中所有KEY的数组（ARRAY）  
 *   
 * 例子： var map = new Map();  
 * map.put("key", "value"); var val = map.get("key") ……  
 */  
function Map() {	
    this.elements = new Array();   
  
    // 获取MAP元素个数   
    this.size = function() {   
        return this.elements.length;   
    }   
  
    // 判断MAP是否为空   
    this.isEmpty = function() {   
        return (this.elements.length < 1);   
    }   
  
    // 删除MAP所有元素   
    this.clear = function() {   
        this.elements = new Array();   
    }   
  
    // 向MAP中增加元素（key, value)   
    this.put = function(_key, _value) {   
        this.elements.push({   
                    key : _key,   
                    value : _value   
                });   
    }   
       
    // 增加元素并覆盖   
    this.putOverride = function(_key,_value){   
        this.remove(_key);   
        this.put(_key,_value);   
    }   
  
    // 删除指定KEY的元素，成功返回True，失败返回False   
    this.remove = function(_key) {   
        var bln = false;   
        try {   
            for (i = 0; i < this.elements.length; i++) {   
                if (this.elements[i].key == _key) {   
                    this.elements.splice(i, 1);   
                    return true;   
                }   
            }   
        } catch (e) {   
            bln = false;   
        }   
        return bln;   
    }   
  
    // 获取指定KEY的元素值VALUE，失败返回NULL   
    this.get = function(_key) {   
        try {   
            for (i = 0; i < this.elements.length; i++) {   
                if (this.elements[i].key == _key) {   
                    return this.elements[i].value;   
                }   
            }   
        } catch (e) {   
            return null;   
        }   
    }   
  
    // 获取指定索引的元素（使用element.key，element.value获取KEY和VALUE），失败返回NULL   
    this.element = function(_index) {   
        if (_index < 0 || _index >= this.elements.length) {   
            return null;   
        }   
        return this.elements[_index];   
    }   
  
    // 判断MAP中是否含有指定KEY的元素   
    this.containsKey = function(_key) {   
        var bln = false;   
        try {   
            for (i = 0; i < this.elements.length; i++) {   
                if (this.elements[i].key == _key) {   
                    bln = true;   
                }   
            }   
        } catch (e) {   
            bln = false;   
        }   
        return bln;   
    }   
  
    // 判断MAP中是否含有指定VALUE的元素   
    this.containsValue = function(_value) {   
        var bln = false;   
        try {   
            for (i = 0; i < this.elements.length; i++) {   
                if (this.elements[i].value == _value) {   
                    bln = true;   
                }   
            }   
        } catch (e) {   
            bln = false;   
        }   
        return bln;   
    }   
  
    // 获取MAP中所有VALUE的数组（ARRAY）   
    this.values = function() {   
        var arr = new Array();   
        for (i = 0; i < this.elements.length; i++) {   
            arr.push(this.elements[i].value);   
        }   
        return arr;   
    }   
  
    // 获取MAP中所有KEY的数组（ARRAY）   
    this.keys = function() {   
        var arr = new Array();   
        for (i = 0; i < this.elements.length; i++) {   
            arr.push(this.elements[i].key);   
        }   
        return arr;   
    }
    
    // 将Array,copy 
    this.copyArray = function(_Array){
        var len = _Array.length;
    	var arr = new Array();
    	if(arr.constructor == Array){ //二维的
    		for(var i=0; i<len; i++){
    			arr.push(_Array[i]);
    		}
    	} else {//一维的
    		
    	}
    	return arr;
    }
}   
  
/**  
   数字数组，可将字串中的数字提出并加入数组，返回最大最小值，排序  
*/  
function NumberArray(){   
       
    this.elements = new Array();   
    this.addandReturnMax = function(num){   
        this.add(num);   
        this.sort();   
        return this.max();   
    }   
    this.add = function(num){   
        num = num.replace(/\D/g,'');   
        this.elements.push(num);   
    }   
    this.sort = function(){   
        this.elements = this.bubbleSort();   
        return this.elements;   
    }   
    this.max = function(){   
        return this.elements.slice(0,1);   
    }   
    this.min = function(){   
        return this.elements.slice(-1,0);   
    }   
       
    this.bubbleSort = function() {   
        var arr = this.elements;   
        // 外层循环，共要进行arr.length次求最大值操作   
        for (var i = 0; i < arr.length; i++) {   
            // 内层循环，找到第i大的元素，并将其和第i个元素交换   
            for (var j = i; j < arr.length; j++) {   
                if (parseInt(arr[i]) < parseInt(arr[j])) {   
                    // 交换两个元素的位置   
                    var temp = arr[i];   
                    arr[i] = arr[j];   
                    arr[j] = temp;   
                }   
            }   
        }   
        return arr;   
    }    
    this.clear = function(){   
        this.elements = new Array();   
    }   
}
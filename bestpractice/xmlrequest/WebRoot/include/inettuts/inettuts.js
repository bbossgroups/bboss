/*
 * Script from NETTUTS.com [by James Padolsey] V.2 (ENHANCED, WITH COOKIES!!!)
 * @requires jQuery($), jQuery UI & sortable/draggable UI modules & jQuery COOKIE plugin
 */

var iNettuts = {
    
    jQuery : $,
    
    settings : {
        columns : '.column',
        widgetSelector: '.widget',
        handleSelector: '.widget-head',
        contentSelector: '.widget-content',
        
        /* If you don't want preferences to be saved change this value to
            false, otherwise change it to the name of the cookie: */
        saveToCookie: 'inettuts-widget-preferences',
        
        widgetDefault : {
            movable: true,
            removable: true,
            collapsible: true,
            editable: true,
            colorClasses : ['color-yellow', 'color-red', 'color-blue', 'color-white', 'color-orange', 'color-green']
        },
        widgetIndividual : {}
    },

    init : function () {
        this.attachStylesheet('inettuts.js.css');
        this.sortWidgets();
        this.addWidgetControls();
        this.makeSortable();
    },
    
    getWidgetSettings : function (id) {
        var $ = this.jQuery,
            settings = this.settings;
        return (id&&settings.widgetIndividual[id]) ? $.extend({},settings.widgetDefault,settings.widgetIndividual[id]) : settings.widgetDefault;
    },
    
    addWidgetControls : function () {
        var iNettuts = this,
            $ = this.jQuery,
            settings = this.settings;
            
        $(settings.widgetSelector, $(settings.columns)).each(function () {
            var thisWidgetSettings = iNettuts.getWidgetSettings(this.id);
            if (thisWidgetSettings.removable) {
                $('<a href="#" class="remove">CLOSE</a>').mousedown(function (e) {
                    /* STOP event bubbling */
                    e.stopPropagation();    
                }).click(function () {
                    if(confirm('This widget will be removed, ok?')) {
                        $(this).parents(settings.widgetSelector).animate({
                            opacity: 0    
                        },function () {
                            $(this).wrap('<div/>').parent().slideUp(function () {
                                $(this).remove();
                            });
                        });
                    }
                    return false;
                }).appendTo($(settings.handleSelector, this));
            }
            
            if (thisWidgetSettings.editable) {
                $('<a href="#" class="edit">EDIT</a>').mousedown(function (e) {
                    /* STOP event bubbling */
                    e.stopPropagation();    
                }).toggle(function () {
                    $(this).css({backgroundPosition: '-66px 0', width: '55px'})
                        .parents(settings.widgetSelector)
                            .find('.edit-box').show().find('input').focus();
                    return false;
                },function () {
                    $(this).css({backgroundPosition: '', width: '24px'})
                        .parents(settings.widgetSelector)
                            .find('.edit-box').hide();
                    return false;
                }).appendTo($(settings.handleSelector,this));
                $('<div class="edit-box" style="display:none;"/>')
                    .append('<ul><li class="item"><label>Change the title?</label><input value="' + $('h3',this).text() + '"/></li>')
                    .append((function(){
                        var colorList = '<li class="item"><label>Available colors:</label><ul class="colors">';
                        $(thisWidgetSettings.colorClasses).each(function () {
                            colorList += '<li class="' + this + '"/>';
                        });
                        return colorList + '</ul>';
                    })())
                    .append('</ul>')
                    .insertAfter($(settings.handleSelector,this));
            }
            
            if (thisWidgetSettings.collapsible) {
                $('<a href="#" class="collapse">COLLAPSE</a>').mousedown(function (e) {
                    /* STOP event bubbling */
                    e.stopPropagation();    
                }).click(function(){
                    $(this).parents(settings.widgetSelector).toggleClass('collapsed');
                    /* Save prefs to cookie: */
                    iNettuts.savePreferences();
                    return false;    
                }).prependTo($(settings.handleSelector,this));
            }
        });
        
        $('.edit-box').each(function () {
            $('input',this).keyup(function () {
                $(this).parents(settings.widgetSelector).find('h3').text( $(this).val().length>20 ? $(this).val().substr(0,20)+'...' : $(this).val() );
                iNettuts.savePreferences();
            });
            $('ul.colors li',this).click(function () {
                
                var colorStylePattern = /\bcolor-[\w]{1,}\b/,
                    thisWidgetColorClass = $(this).parents(settings.widgetSelector).attr('class').match(colorStylePattern)
                if (thisWidgetColorClass) {
                    $(this).parents(settings.widgetSelector)
                        .removeClass(thisWidgetColorClass[0])
                        .addClass($(this).attr('class').match(colorStylePattern)[0]);
                    /* Save prefs to cookie: */
                    iNettuts.savePreferences();
                }
                return false;
                
            });
        });
        
    },
    
    attachStylesheet : function (href) {
        var $ = this.jQuery;
        return $('<link href="' + href + '" rel="stylesheet" type="text/css" />').appendTo('head');
    },
    
    makeSortable : function () {
        var iNettuts = this,
            $ = this.jQuery,
            settings = this.settings,
            $sortableItems = (function () {
                var notSortable = '';
                $(settings.widgetSelector,$(settings.columns)).each(function (i) {
                    if (!iNettuts.getWidgetSettings(this.id).movable) {
                        if(!this.id) {
                            this.id = 'widget-no-id-' + i;
                        }
                        notSortable += '#' + this.id + ',';
                    }
                });
                return $('> li:not(' + notSortable + ')', settings.columns);
            })();
        
        $sortableItems.find(settings.handleSelector).css({
            cursor: 'move'
        }).mousedown(function (e) {
            $sortableItems.css({width:''});
            $(this).parent().css({
                width: $(this).parent().width() + 'px'
            });
        }).mouseup(function () {
            if(!$(this).parent().hasClass('dragging')) {
                $(this).parent().css({width:''});
            } else {
                $(settings.columns).sortable('disable');
            }
        });

        $(settings.columns).sortable({
            items: $sortableItems,
            connectWith: $(settings.columns),
            handle: settings.handleSelector,
            placeholder: 'widget-placeholder',
            forcePlaceholderSize: true,
            revert: 300,
            delay: 100,
            opacity: 0.8,
            containment: 'document',
            start: function (e,ui) {
                $(ui.helper).addClass('dragging');
            },
            stop: function (e,ui) {
                $(ui.item).css({width:''}).removeClass('dragging');
                $(settings.columns).sortable('enable');
                /* Save prefs to cookie: */
                iNettuts.savePreferences();
            }
        });
    },
    
    savePreferences : function () {
        var iNettuts = this,
            $ = this.jQuery,
            settings = this.settings,
            cookieString = '';
            
        if(!settings.saveToCookie) {return;}
        
        /* Assemble the cookie string */
        $(settings.columns).each(function(i){
            cookieString += (i===0) ? '' : '|';
            $(settings.widgetSelector,this).each(function(i){
                cookieString += (i===0) ? '' : ';';
                /* ID of widget: */
                cookieString += $(this).attr('id') + ',';
                /* Color of widget (color classes) */
                cookieString += $(this).attr('class').match(/\bcolor-[\w]{1,}\b/) + ',';
                /* Title of widget (replaced used characters) */
                cookieString += $('h3:eq(0)',this).text().replace(/\|/g,'[-PIPE-]').replace(/,/g,'[-COMMA-]') + ',';
                /* Collapsed/not collapsed widget? : */
                cookieString += $(settings.contentSelector,this).css('display') === 'none' ? 'collapsed' : 'not-collapsed';
            });
        });
        $.cookie(settings.saveToCookie,cookieString,{
            expires: 10
            //path: '/'
        });
    },
    
    sortWidgets : function () {
        var iNettuts = this,
            $ = this.jQuery,
            settings = this.settings;
        
        /* Read cookie: */
        var cookie = $.cookie(settings.saveToCookie);
        if(!settings.saveToCookie||!cookie) {
            /* Get rid of loading gif and show columns: */
            $('body').css({background:'#000'});
            $(settings.columns).css({visibility:'visible'});
            return;
        }
        
        /* For each column */
        $(settings.columns).each(function(i){
            
            var thisColumn = $(this),
                widgetData = cookie.split('|')[i].split(';');
                
            $(widgetData).each(function(){
                if(!this.length) {return;}
                var thisWidgetData = this.split(','),
                    clonedWidget = $('#' + thisWidgetData[0]),
                    colorStylePattern = /\bcolor-[\w]{1,}\b/,
                    thisWidgetColorClass = $(clonedWidget).attr('class').match(colorStylePattern);
                
                /* Add/Replace new colour class: */
                if (thisWidgetColorClass) {
                    $(clonedWidget).removeClass(thisWidgetColorClass[0]).addClass(thisWidgetData[1]);
                }
                
                /* Add/replace new title (Bring back reserved characters): */
                $(clonedWidget).find('h3:eq(0)').html(thisWidgetData[2].replace(/\[-PIPE-\]/g,'|').replace(/\[-COMMA-\]/g,','));
                
                /* Modify collapsed state if needed: */
                if(thisWidgetData[3]==='collapsed') {
                    /* Set CSS styles so widget is in COLLAPSED state */
                    $(clonedWidget).addClass('collapsed');
                }

                $('#' + thisWidgetData[0]).remove();
                $(thisColumn).append(clonedWidget);
            });
        });
        
        /* All done, remove loading gif and show columns: */
        $('body').css({background:'#000'});
        $(settings.columns).css({visibility:'visible'});
    }
  
};

iNettuts.init();
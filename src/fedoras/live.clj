(ns fedoras.live
  "Editable, runnable code cells for the HTML edition.

  There are two kinds of code in this book and it is worth being precise
  about the difference:

  1. NOTEBOOK CODE. Ordinary top-level forms in the scene files. Clay
     evaluates them on the JVM at build time and prints the result into
     the page. This is his actual engine -- it is `require`d from
     `src/fedoras/engine/`, it is real code in a real project, and it
     runs when you build.

  2. LIVE CELLS. `editable` blocks, below. These ship the source to the
     reader's browser, where Scittle (ClojureScript in a script tag)
     evaluates whatever they type. The reader can break it, fix it, and
     break it again. Nothing is sent anywhere; it runs client-side.

  Live cells are the interactive layer, so keep them small and
  self-contained -- a live cell has no access to the JVM namespaces.
  If a snippet needs the engine, make it notebook code instead.

  STATUS: experimental. If Scittle fails to load (offline reader, strict
  CSP, PDF edition), the cell degrades to a visible code block with a
  Run button that reports that the runtime is missing. Nothing breaks."
  (:require [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]))

(def ^:private splitter-js
  "JS. Defines window.__fedorasSplitForms only if it isn't defined yet,
   so a page with many cells does not redefine it and a page with one
   cell still gets it."
  "if (typeof window.__fedorasSplitForms === 'undefined') {
     window.__fedorasSplitForms = function(s){
       var out=[];var depth=0;var start=-1;var inStr=false;var inComment=false;
       for(var i=0;i<s.length;i++){
         var c=s[i];
         if(inComment){if(c==='\\n')inComment=false;continue;}
         if(inStr){
           if(c==='\\\\'){i++;continue;}
           if(c==='\"'){inStr=false;}
           continue;
         }
         if(c===';'){inComment=true;continue;}
         if(c==='\"'){inStr=true;continue;}
         if(c==='('||c==='['||c==='{'){
           if(depth===0)start=i;depth++;continue;
         }
         if(c===')'||c===']'||c==='}'){
           depth--;
           if(depth===0&&start>=0){out.push(s.substring(start,i+1));start=-1;}
           continue;
         }
       }
       return out;
     };
   }")

(defn- run-js [id]
  (str "(function(){"
       "var s=document.getElementById('" id "-src').value;"
       "var o=document.getElementById('" id "-out');"
       "if(typeof scittle==='undefined'){"
       "o.textContent='(ClojureScript runtime unavailable.)';return;}"
       "try{"
       "  var forms=window.__fedorasSplitForms(s);"
       "  var lines=[];"
       "  forms.forEach(function(f){"
       "    var t=f.replace(/^\\s+/,'');"
       "    var isDef=/^\\(def[n]?\\s/.test(t);"
       "    try{"
       "      var v=scittle.core.eval_string('(pr-str '+f+')');"
       "      if(!isDef){"
       "        lines.push((v===null||v===undefined)?'nil':String(v));"
       "      }"
       "    }catch(e){lines.push('ERR: '+String(e));}"
       "  });"
       "  o.textContent=lines.join('\\n');"
       "}catch(e){o.textContent=String(e);}"
       "})()"))

(defn editable
  ([code] (editable {} code))
  ([{:keys [id label rows]} code]
   (let [id   (or id (str "live-" (str/replace (str (random-uuid)) #"-" "")))
         code (str/trim code)
         rows (or rows (max 3 (inc (count (str/split-lines code)))))]
     (kind/hiccup
      [:div.live-cell
       [:script {:type "application/javascript"} splitter-js]
       [:div.live-label (or label "Edit and run")]
       [:textarea {:id (str id "-src") :class "live-src" :rows rows :spellcheck "false"}
        code]
       [:div.live-bar
        [:button {:class "live-run" :onclick (run-js id)} "Run"]]
       [:pre {:id (str id "-out") :class "live-out"}]]))))
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{metu}[2003/06/20 METU Thesis Class]

%default options

%spacing
\newif\ifd@uble \d@ubletrue

%page numbering
\newif\ifpn@top \pn@topfalse
\newif\ifpn@topr \pn@toprtrue

%chapter numbering (roman/arabic)
\newif\if@rmnchp \@rmnchptrue

%table stuff
\newif\ifmy@t@ble \my@t@blefalse
\newif\ifmy@figur@ \my@figur@false
\newif\ifmy@t@ble \my@t@blefalse

%font size
\newif\if@tenpt \@tenptfalse
\newif\if@elevenpt \@elevenpttrue
\newif\if@twelvept \@twelveptfalse

% seems we need it.
\newif\if@twoside
\@twosidetrue \@mparswitchtrue \def\ds@draft{\overfullrule 5pt}

%Font Options
\DeclareOption{10pt}{\@tenpttrue \@elevenptfalse \@twelveptfalse}
\DeclareOption{11pt}{\@tenptfalse \@elevenpttrue \@twelveptfalse}
\DeclareOption{12pt}{\@tenptfalse \@elevenptfalse \@twelvepttrue}

%Page Numbering
\DeclareOption{pntr}{\pn@toptrue \pn@toprtrue}
\DeclareOption{pntc}{\pn@toptrue \pn@toprfalse}
\DeclareOption{pnbt}{\pn@topfalse \pn@toprfalse}

%Chapter Numbering
\DeclareOption{chaproman}{\@rmnchptrue}
\DeclareOption{chaparabic}{\@rmnchpfalse}

%Spacing
\DeclareOption{double}{\d@ubletrue}
\DeclareOption{oneandhalf}{\d@ublefalse}

%Unspecified input handler
\DeclareOption*{Invalid Option \CurrentOption }


\ProcessOptions\relax
%\LoadClass{book}
\RequirePackage[utf8]{inputenc}

% Font naming
\DeclareOldFontCommand{\rm}{\normalfont\rmfamily}{\mathrm}
\DeclareOldFontCommand{\sf}{\normalfont\sffamily}{\mathsf}
\DeclareOldFontCommand{\tt}{\normalfont\ttfamily}{\mathtt}
\DeclareOldFontCommand{\bf}{\normalfont\bfseries}{\mathbf}
\DeclareOldFontCommand{\it}{\normalfont\itshape}{\mathit}
\DeclareOldFontCommand{\sl}{\normalfont\slshape}{\@nomath\sl}
\DeclareOldFontCommand{\sc}{\normalfont\scshape}{\@nomath\sc}
\DeclareRobustCommand*\cal{\@fontswitch\relax\mathcal}
\DeclareRobustCommand*\mit{\@fontswitch\relax\mathnormal}

%input the appropriate .def file
\if@tenpt \input{metu10.def} \fi
\if@elevenpt \input{metu11.def} \fi
\if@twelvept \input{metu12.def} \fi

%\ifpn@top\footheight=0cm\footskip=0cm%
%\else\footheight=12pt\footskip=30pt\headsep=0cm\headheight=0cm\fi

\ifpn@top
    \footskip=0cm%
\else
    \footskip=30pt\headsep=0cm\headheight=0cm
\fi

%\def\expthr@@#1{%
%   \expandafter\expandafter\expandafter%
%   \uplowcase\expandafter{#1}}
%\def\expthr@@#1{ \expandafter\expandafter\expandafter\uplowcase{#1}}
\def\expthr@@#1{#1}

\newtoks\@tmp

\def\uplowcase#1{\textsc{#1}}

\newtoks\@last \newtoks\@name

\def\i@uthor#1 #2.mend{\def\paramtw@{#2}\ifx\paramtw@\empty\@last={#1}%
\else\ifx\t@r\empty\@name={#1}\def\t@r{t@r}\else%
\@name=\expandafter{\the\@name{} #1}\fi\i@uthor#2.mend\fi}
\def\inv@uthor#1{\def\t@r{}\@name={}\@last={}\i@uthor#1 .mend%
\expthr@@{\the\@last}, \expthr@@{\the\@name}}

\newcommand{\singlespacing}{\renewcommand{\baselinestretch}{1}%
\let\@tmpcurrentsize=\currentsize \footnotesize \@tmpcurrentsize}
\newcommand{\doublespacing}{\renewcommand{\baselinestretch}{2}
\let\@tmpcurrentsize=\currentsize \footnotesize \@tmpcurrentsize}
\newcommand{\oneandhalfspacing}{\renewcommand{\baselinestretch}{1.5}
\let\@tmpcurrentsize=\currentsize \footnotesize \@tmpcurrentsize}
\def\@prelimsize{\@setsize\@prelimsize{14pt}\xiipt\@xiipt
\let\currentsize=\@prelimsize}

\let\f@@tnote\footnote
\def\footnote#1{\f@@tnote{\singlespacing #1}}

\def\labelenumi{\arabic{enumi}.}
\def\theenumi{\arabic{enumi}}
\def\labelenumii{(\alph{enumii})}
\def\theenumii{\alph{enumii}}
\def\p@enumii{\theenumi}
\def\labelenumiii{\roman{enumiii}.}
\def\theenumiii{\roman{enumiii}}
\def\p@enumiii{\theenumi(\theenumii)}
\def\labelenumiv{\Alph{enumiv}.}
\def\theenumiv{\Alph{enumiv}}
\def\p@enumiv{\p@enumiii\theenumiii}

\def\labelitemi{$\bullet$}
\def\labelitemii{\bf --}
\def\labelitemiii{$\ast$}
\def\labelitemiv{$\cdot$}

\def\verse{\let\\=\@centercr
 \list{}{\itemsep\z@ \itemindent -1.5em\listparindent \itemindent
 \rightmargin\leftmargin\advance\leftmargin 1.5em}\item[]}
\let\endverse\endlist
\def\quotation{\list{}{\listparindent 1.5em
 \itemindent\listparindent
 \rightmargin\leftmargin\parsep 0pt plus 1pt}\item[]}
\let\endquotation=\endlist
\def\quote{\par \singlespacing \list{}{\rightmargin\leftmargin}\item[]}
\let\endquote=\endlist

\def\descriptionlabel#1{\hspace\labelsep \bf #1}
\def\description{\list{}{\labelwidth\z@ \itemindent-\leftmargin
 \let\makelabel\descriptionlabel}}
\let\enddescription\endlist
\newdimen\descriptionmargin
\descriptionmargin=3em

\def\titlepage{\@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn
 \else \newpage \fi \@prelimsize \pagestyle{empty}}
\def\endtitlepage{\if@restonecol\twocolumn \else \newpage \fi
\pagestyle{plain}\normalsize}

\def\approvalpage{\@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn
 \else \newpage \fi \@prelimsize \pagestyle{empty}}
\def\endapprovalpage{\if@restonecol\twocolumn \else \newpage \fi
\pagestyle{plain}\normalsize}

\arraycolsep 5pt \tabcolsep 6pt \arrayrulewidth .4pt \doublerulesep 2pt
\tabbingsep \labelsep
\skip\@mpfootins = \skip\footins
\fboxsep = 3pt \fboxrule = .4pt

\newcounter {part}
\newcounter {chapter}
\newcounter {section}[chapter]
\newcounter {subsection}[section]
\newcounter {subsubsection}[subsection]
\newcounter {paragraph}[subsubsection]
\newcounter {subparagraph}[paragraph]

\def\thepart {\Roman{part}}
\if@rmnchp \def\thechapter{\Roman{chapter}}
  \else \def\thechapter{\arabic{chapter}} \fi
\def\thesection {\thechapter.\arabic{section}}
\def\thesubsection {\thesection.\arabic{subsection}}
\def\thesubsubsection {\thesubsection .\arabic{subsubsection}}
\def\theparagraph {\thesubsubsection.\arabic{paragraph}}
\def\thesubparagraph {\theparagraph.\arabic{subparagraph}}
\def\@chapapp{\vskip4mm CHAPTER}
\def\@chapappendix{APPENDIX}

\def\@pnumwidth{1.55em}
\def\@tocrmarg {2.55em}
\def\@dotsep{4.5}
\setcounter{tocdepth}{6}

\def\tableofcontents{\singlespacing
\@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn
 \fi\chapter*{\vskip1.5mm TABLE OF CONTENTS\@mkboth{CONTENTS}{CONTENTS}}
\addcontentsline{toc}{leads}{\protect\numberline {}TABLE OF CONTENTS}
 \@starttoc{toc}\if@restonecol\twocolumn\fi\doublespacing\newpage }

\def\l@part#1#2{\addpenalty{-\@highpenalty}
 \addvspace{2.25em plus 1pt} \begingroup
 \@tempdima 3em \parindent \z@ \rightskip \@pnumwidth \parfillskip
-\@pnumwidth
 {\large \bf \leavevmode #1\hfil \hbox to\@pnumwidth{\hss #2}}\par
 \nobreak \endgroup}
\def\l@leads{\vskip 1.0em plus 1pt\@dottedtocline{1}{0em}{0em}}
\def\l@nodots#1#2{\vskip 1.0em plus 1pt\pagebreak[3]\noindent #1\par
  \vskip 0.5em}
\def\l@chapter{\pagebreak[3]
  \vskip 0.5em plus 1pt\@dottedtocline{1}{1.5em}{2.3em}}
\def\l@section{\vskip 0.5em plus 1pt\@dottedtocline{1}{3.8em}{3.5em}}
\def\l@subsection{\vskip 0.5em plus 1pt\@dottedtocline{2}{7.3em}{4.1em}}
\def\l@subsubsection{\vskip 0.5em plus 1pt\@dottedtocline{3}{11.4em}{5em}}
\def\l@paragraph{\vskip 0.5em plus 1pt\@dottedtocline{4}{16.4em}{5em}}
\def\l@subparagraph{\vskip 0.5em plus 1pt\@dottedtocline{5}{21.4em}{6em}}

% \def\appendices{\oneandhalfspacing
% \@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn
%  \fi\chapter*{APPENDICES\@mkboth {APPENDICES}{APPENDICES}}
% \addcontentsline{toc}{nodots}{\protect\numberline {}APPENDICES}
%     \setcounter{chapter}{0}\setcounter{section}{0}\setcounter{subsection}{0}
%     \renewcommand{\@chapapp}{\appendixname}
%     \def\thechapter{\Alph{chapter}}
% }
\def\appendix{\par
 \setcounter{chapter}{0}
 \setcounter{section}{0}
 \addcontentsline{toc}{nodots}{\protect\numberline {}APPENDICES}
 \def\@chapapp{APPENDIX}
 \def\thechapter{\Alph{chapter}}}

\def\listoffigures{\oneandhalfspacing
\@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn
 \fi\chapter*{\vskip-1mm LIST OF FIGURES\@mkboth
 {LIST OF FIGURES}{LIST OF FIGURES}}
\addcontentsline{toc}{leads}{\protect\numberline {}LIST OF FIGURES}
\@starttoc{lof}\if@restonecol
 \twocolumn\fi\doublespacing\newpage }
\def\l@figure{\@myfiguredottedtocline{1}{0em}{2.0em}}

\def\listoftables{\oneandhalfspacing
\@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn
 \fi\chapter*{\vskip-1mm LIST OF TABLES\@mkboth
 {LIST OF TABLES}{LIST OF TABLES}}
\addcontentsline{toc}{leads}{\protect\numberline {}LIST OF TABLES}
\@starttoc{lot}\if@restonecol
 \twocolumn\fi\doublespacing\newpage }
\def\l@table{\@mytabledottedtocline{1}{0em}{2.0em}}

\def\theglossary#1{\def\he@dgl@{\singlespacing
\@restonecoltrue \if@twocolumn\@restonecolfalse
 \fi\columnseprule \z@ \columnsep 35pt\twocolumn
 \chapter*{\vskip 3.5mm \uppercase\expandafter{\@symb@l}%
\@mkboth{\uppercase{\@symb@l}}{\uppercase{\@symb@l}}}%
\parindent \z@ \parskip \z@ plus .3pt\relax }%
 \def\symb@l[##1]{\def\@symb@l{##1}\he@dgl@}%
\@ifnextchar[{\symb@l}{\symb@l[LIST OF ABBREVIATIONS]}%
\addcontentsline{toc}{leads}{\protect\numberline {}LIST OF ABBREVIATIONS}
\begin{list}{}{\settowidth\labelwidth{[#1]}\leftmargin\labelwidth %labelsep 0.1cm
 \advance\leftmargin\labelsep \itemsep \z@}\def\makelabel##1{##1 \hfil}}%
\def\endtheglossary{\end{list}\if@restonecol\onecolumn\else\clearpage\fi}

%If you have problems with the position of ABBREVATIONS title, you may try to 
%(1) commenting out the above lines, and
%(2) uncomment the following lines.

%\def\glossary{\@ifnextchar[\gl@ssary{\gl@ssary[ABBREVIATIONS]}}
%\def\endglossary{\endgl@ssary}
%\def\gl@ssary[#1]{\@restonecoltrue\if@twocolumn\@restonecolfalse\fi\columnseprule
%\z@
%\columnsep 35pt%\twocolumn
%\chapter*{\vskip *-5mm* LIST OF #1}
%\@mkboth {#1}{#1}
%\addcontentsline{toc}{leads}{\protect\numberline {}LIST OF
%\uppercase{#1}}\singlespacing}
%\def\endgl@ssary{\if@restonecol\onecolumn\else\clearpage\fi}

\def\listofillustrations{\singlespacing
\addcontentsline{toc}{leads}{\protect\numberline {}LIST OF ILLUSTRATIONS}
\@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn\fi
 \chapter*{LIST OF ILLUSTRATIONS\@mkboth
 {LIST OF ILLUSTRATIONS}{LIST OF ILLUSTRATIONS}}\@starttoc{loi}\if@restonecol
 \twocolumn\fi\doublespacing\newpage }
\def\l@illustration{\@dottedtocline{1}{1.5em}{2.3em}}


\def\@themybib[#1]#2{\def\@symb@l{#1} \ifx\@symb@l\empty
 \def\@symb@l{REFERENCES} \fi
 \singlespacing \@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn\fi
 \chapter*{\vskip1mm \uppercase\expandafter{\@symb@l}
 \@mkboth{\uppercase{\@symb@l}}{\uppercase{\@symb@l}}}
  \addcontentsline{toc}{leads}{\protect\numberline {}\uppercase{REFERENCES}}
 \list{[\arabic{enumi}]}{\settowidth\labelwidth{[#2]}\leftmargin\labelwidth
 \advance\leftmargin\labelsep\usecounter{enumi}}
 \def\newblock{\hskip .11em plus .33em minus -.07em}
 \sloppy\clubpenalty4000\widowpenalty4000
 \sfcode`\.=1000\relax}
\def\thebibliography{
\@ifnextchar[{\@themybib}{\@themybib[]}
}
\def\endthebibliography{\endlist}

%\let\endthebibliography=\endlist

\newif\if@restonecol
\def\theindex{\@restonecoltrue\if@twocolumn\@restonecolfalse\fi
\columnseprule \z@
\columnsep 35pt\twocolumn[\@makeschapterhead{Index}]
 \@mkboth{INDEX}{INDEX}\thispagestyle{plain}\parindent\z@
 \parskip\z@ plus .3pt\relax\let\item\@idxitem}
\def\@idxitem{\par\hangindent 40pt}
\def\subitem{\par\hangindent 40pt \hspace*{20pt}}
\def\subsubitem{\par\hangindent 40pt \hspace*{30pt}}
\def\endtheindex{\if@restonecol\onecolumn\else\clearpage\fi}
\def\indexspace{\par \vskip 10pt plus 5pt minus 3pt\relax}

\def\footnoterule{\kern-3\p@
 \hrule width \columnwidth
 \kern 2.6\p@}
\@addtoreset{footnote}{chapter}
\long\def\@makefntext#1{\parindent 1em\noindent
 \hbox to 1.8em{\hss$^{\@thefnmark}$}#1}

\setcounter{topnumber}{2}
\def\topfraction{.7}
\setcounter{bottomnumber}{1}
\def\bottomfraction{.3}
\setcounter{totalnumber}{3}
\def\textfraction{.2}
\def\floatpagefraction{.5}
\setcounter{dbltopnumber}{2}
\def\dbltopfraction{.7}
\def\dblfloatpagefraction{.5}

%captionlar icin buraya bak
\long\def\@makecaption#1#2{
 \ifmy@figur@ \vskip 20pt\fi
 \setbox\@tempboxa\hbox{#1: #2}
 \ifdim \wd\@tempboxa >\hsize #1: #2\par \else \hbox
to\hsize{\hfil\box\@tempboxa\hfil}
 \vskip7.5pt
 \fi \ifmy@t@ble \vskip 20pt\fi}

\newcounter{illustration}[chapter]
\def\theillustration{\thechapter.\@arabic\c@illustration}
\def\fps@illustration{tbp}
\def\ftype@illustration{3}
\def\ext@illustration{loi}
\def\fnum@illustration{Illustration \theillustration}
\def\illustration{\singlespacing \my@figur@true \@float{illustration}}
\let\endillustration\end@float
\@namedef{illustration*}{\@dblfloat{illustration}}
\@namedef{endillustration*}{\end@dblfloat}

\newcounter{figure}[chapter]
\def\thefigure{\thechapter.\@arabic\c@figure}
\def\fps@figure{tbp}
\def\ftype@figure{1}
\def\ext@figure{lof}
\def\fnum@figure{Figure \thefigure}
\def\figure{\singlespacing \my@figur@true \@float{figure}}
\let\endfigure\end@float
\@namedef{figure*}{\@float{figure}}
\@namedef{endfigure*}{\end@float}

\newcounter{table}[chapter]
\def\thetable{\thechapter.\@arabic\c@table}
\def\fps@table{tbp}
\def\ftype@table{2}
\def\ext@table{lot}
\def\fnum@table{Table \thetable}
\def\table{\singlespacing \my@t@bletrue \@float{table}}
\let\endtable\end@float
\@namedef{table*}{\@float{table}}
\@namedef{endtable*}{\end@float}

\def\@graduateschool{}\def\graduateschool#1{\def\@graduateschool{#1}}
\def\@department{}\def\department#1{\def\@department{#1}}
\def\@departmentofsupervisor{}\def\departmentofsupervisor#1{\def\@departmentofsupervisor{#1}}
\def\@departmentofcosupervisor{}\def\departmentofcosupervisor#1{\def\@departmentofcosupervisor{#1}}
\def\@tdepartment{}\def\turkishdepartment#1{\def\@tdepartment{#1}}
\def\@degree{}\def\degree#1{\def\@degree{#1}}
\def\@tdegree{}\def\turkishdegree#1{\def\@tdegree{#1}}
\def\@shortdegree{}\def\shortdegree#1{\def\@shortdegree{#1}}
\def\@ttitle{}\def\turkishtitle#1{\def\@ttitle{#1}}
\def\@director{} \def\director#1{\def\@director{#1}}
\def\@headofdept{} \def\headofdept#1{\def\@headofdept{#1}}
\newif\ifexist@co \exist@cofalse
\def\@cosupervisor{} \def\cosupervisor#1{\def\@cosupervisor{#1} \exist@cotrue}
\def\@supervisor{} \def\supervisor#1{\def\@supervisor{#1}}
\def\@turkishcosupervisor{} \def\turkishcosupervisor#1{\def\@turkishcosupervisor{#1} \exist@cotrue}
\def\@turkishsupervisor{} \def\turkishsupervisor#1{\def\@turkishsupervisor{#1}}
\def\@affiliationi{} \def\affiliationi#1{\def\@affiliationi{#1}}
\def\@affiliationii{} \def\affiliationii#1{\def\@affiliationii{#1}}
\def\@affiliationiii{} \def\affiliationiii#1{\def\@affiliationiii{#1}}
\def\@affiliationiv{} \def\affiliationiv#1{\def\@affiliationiv{#1}}
\def\@affiliationv{} \def\affiliationv#1{\def\@affiliationv{#1}}
\def\@committeememberi{} \def\committeememberi#1{\def\@committeememberi{#1}}
\def\@committeememberii{} \def\committeememberii#1{\def\@committeememberii{#1}}
\def\@committeememberiii{} \def\committeememberiii#1{\def\@committeememberiii{#1}}
\def\@committeememberiv{} \def\committeememberiv#1{\def\@committeememberiv{#1}}
\def\@committeememberv{} \def\committeememberv#1{\def\@committeememberv{#1}}
\def\@keywords{} 
\def\keywords#1{\def\@keywords{#1}}
\def\@anahtarklm{} \def\anahtarklm#1{\def\@anahtarklm{#1}}
\def\@authoruppercase{}\def\authoruppercase#1{\def\@authoruppercase{#1}}
\def\turkishdate#1{\def\@tdate{#1}}

\def\maketitle{\begin{titlepage}\normalsize
\let\footnotesize\small \let\footnoterule\relax \setcounter{page}{1}
\null \nointerlineskip
\vskip 0.8cm \begin{center} \nointerlineskip
 \vbox{\singlespacing \uppercase\expandafter{\@title}}
\vskip 4.5cm plus0pt minus1cm \nointerlineskip
 \vbox{\singlespacing A THESIS SUBMITTED TO\\
       THE GRADUATE SCHOOL OF \uppercase\expandafter{\@graduateschool}\\
       OF\\ MIDDLE EAST TECHNICAL UNIVERSITY}
\vskip 1.85cm plus0pt minus2mm \nointerlineskip
 \vbox{BY}
\vskip 1.85cm plus0pt minus2mm \nointerlineskip
 \vbox{\expandafter{\@authoruppercase}}
\vskip 5cm plus0pt minus1cm \nointerlineskip
 \vbox{\singlespacing
       IN PARTIAL FULFILLMENT OF THE REQUIREMENTS\\
       FOR\\
       THE DEGREE OF \uppercase\expandafter{\@degree}\\
       IN\\
       \uppercase\expandafter{\@department}}
\vskip 5cm plus0pt minus1cm \nointerlineskip
\vspace{\stretch{1}}
 \vbox{\uppercase\expandafter{\@date}} \end{center}
\end{titlepage}
\setcounter{footnote}{0} \let\thanks\relax
\gdef\@thanks{}\let\maketitle\relax}

\def\makeapproval{\begin{approvalpage} \normalsize \parskip \z@
\parskip \z@ \topsep \z@ \partopsep \z@ \parsep \z@ \itemsep \z@
\null \nointerlineskip \parindent=0pt
\vskip1.0cm plus0pt minus2mm \nointerlineskip
\parbox[t]{15cm}{\begin{center}Approval of the thesis:\end{center}}
\vskip1.0cm plus0pt minus2mm \nointerlineskip
\parbox[t]{15cm}{\begin{center}\bf{\uppercase\expandafter{\@title}}\end{center}}
\vskip2.0cm plus0pt minus2mm \nointerlineskip
\parbox[t]{14.5cm}{\singlespacing submitted by {\bf \@authoruppercase} in partial fulfillment of the requirements for the degree of \\{\bf \@degree~ in \@department~ Department, Middle East Technical University} by,}
\vskip1.5cm plus0pt minus2mm \nointerlineskip
\vbox{\parbox[t]{11cm}{\singlespacing \expthr@@{\@director}\\
      Dean, Graduate School of {\bf Natural and Applied Sciences}} \hfill
      \parbox[t]{3cm}{\singlespacing \begin{center}
      \rule{3cm}{1pt} \\
      \end{center}\nointerlineskip}}
\vskip0.5cm plus0pt minus2mm \nointerlineskip
 \vbox{\parbox{11cm}{\singlespacing \expthr@@{\@headofdept}\\
	Head of Department, {\bf \expthr@@{\@department}}} \hfill \parbox{3cm}{\singlespacing \begin{center}
                      \rule{3cm}{1pt}
                      \end{center}\nointerlineskip}}
\vskip0.5cm plus0pt minus2mm \nointerlineskip
\vbox{
		\parbox{11cm}{\singlespacing \expthr@@{\@supervisor}\\
		Supervisor, {\bf \expthr@@{\@departmentofsupervisor}}} \hfill \parbox{3cm}{\singlespacing \begin{center}
                      \rule{3cm}{1pt}
                      \end{center}\nointerlineskip}}
\vskip0.5cm plus0pt minus2mm \nointerlineskip
\ifexist@co
  \vbox{
  \parbox{11cm}{\singlespacing \expthr@@{\@cosupervisor}\\ Co-supervisor, {\bf \expthr@@{\@departmentofcosupervisor}}} \hfill \parbox{3cm}{\singlespacing \begin{center} \rule{3cm}{1pt} \end{center}\nointerlineskip}}
\else
  \vbox{\parbox{11cm}{~\\~} \hfill \parbox{3cm}{~\\~}}
\fi
\vskip1.5cm plus0pt minus2mm \nointerlineskip
 \vbox{\bf{Examining Committee Members:}}
\vskip0.5cm plus0pt minus2mm \nointerlineskip
 \vbox{\parbox{14.25cm}{\singlespacing \expthr@@{\@committeememberi \\ \expthr@@{\@affiliationi}}
 \hfill \rule{3cm}{1pt}}}
\vskip0.5cm plus0pt minus2mm \nointerlineskip
 \vbox{\parbox{14.25cm}{\singlespacing \expthr@@{\@committeememberii \\ \expthr@@{\@affiliationii}}
 \hfill \rule{3cm}{1pt}}}
\vskip0.5cm plus0pt minus2mm \nointerlineskip
 \vbox{\parbox{14.25cm}{\singlespacing \expthr@@{\@committeememberiii \\ \expthr@@{\@affiliationiii}}
 \hfill \rule{3cm}{1pt}}}
\vskip0.5cm plus0pt minus2mm \nointerlineskip
 \vbox{\parbox{14.25cm}{\singlespacing \expthr@@{\@committeememberiv \\ \expthr@@{\@affiliationiv}}
 \hfill \rule{3cm}{1pt}}}
\vskip0.5cm plus0pt minus2mm \nointerlineskip
 \vbox{\parbox{14.25cm}{\singlespacing \expthr@@{\@committeememberv \\ \expthr@@{\@affiliationv}}
 \hfill \rule{3cm}{1pt}}}
\vskip1.5cm plus0pt minus2mm \nointerlineskip
 \vbox{\parbox{14.25cm}{\bf  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Date:
 \hfill \rule{3cm}{1pt}}}
\end{approvalpage}
\gdef\@director{}\let\director\relax
\gdef\@headofdept{}\let\headofdept\relax
\gdef\@degree{}\let\degree\relax
\gdef\@committeemember1{}\let\committeemember1\relax
\gdef\@committeemember2{}\let\committeemember2\relax
\gdef\@committeemember3{}\let\committeemember3\relax
\gdef\@committeemember4{}\let\committeemember4\relax
\gdef\@committeemember5{}\let\committeemember5\relax
\gdef\@graduateschool{}\let\graduateschool\relax \let\makeapproval\relax}

\def\oz{\addcontentsline{toc}{leads}{\protect\numberline {}\"OZ}
\@restonecolfalse
\if@twocolumn
    \@restonecoltrue\onecolumn
 \fi
 \chapter*{\vskip2.5mm \"{O}Z}\@mkboth {\"{O}Z}{\"{O}Z} \vskip 1cm
\begin{center}\expandafter{\@ttitle} \\ \vskip 1.0\baselineskip \vskip 1.5cm
 \expandafter\expandafter\expandafter\inv@uthor\expandafter{\@author} \\[0.2cm]
 \begin{tabular}{ll}
 \multicolumn{2}{l}{\@tdegree, \@tdepartment}\\[0.2cm]
 Tez Y\"{o}neticisi & : \expthr@@{\@turkishsupervisor} \\[0.2cm]
 \ifexist@co Ortak Tez Y\"{o}neticisi & : \expthr@@{\@turkishcosupervisor}\fi \\[0.2cm]
\end{tabular}
\vskip \baselineskip \@tdate, \pageref{`@lastpage'} sayfa\end{center}
\vskip \baselineskip}
\def\endoz{\vskip 1.5\baselineskip \par \noindent Anahtar Kelimeler: \@anahtarklm
\gdef\@anahtarklm{}\let\anahtarklm\relax
\gdef\@tdegree{}\let\turkishdegree\relax
\gdef\@tdepartment{}\let\turkishdepartment\relax
\gdef\@ttitle{}\let\turkishtitle\relax
\gdef\@supervisor{}\let\supervisor\relax
\gdef\@cosupervisor{}\let\cosupervisor\relax
\gdef\@authoruppercase{}\let\authoruppercase\relax
\if@restonecol \twocolumn\fi\oneandhalfspacing\newpage }

\def\abstract{\addcontentsline{toc}{leads}{\protect\numberline {}ABSTRACT}
\@restonecolfalse
\if@twocolumn
    \@restonecoltrue\onecolumn
 \fi
 \chapter*{\vskip4mm ABSTRACT}\@mkboth {ABSTRACT}{ABSTRACT} \vskip 1cm
 \begin{center}\uppercase\expandafter{\@title} \\ \vskip 1.0\baselineskip \vskip 1.5cm
 \expandafter\expandafter\expandafter\inv@uthor\expandafter{\@author} \\[0.2cm]
 \begin{tabular}{ll}
 \multicolumn{2}{l}{\@shortdegree, Department of \@department} \\[0.2cm]
 Supervisor & : \expthr@@{\@supervisor} \\[0.2cm]
 \ifexist@co Co-Supervisor & : \expthr@@{\@cosupervisor}\fi\\[0.2cm]
\end{tabular}
\vskip \baselineskip \@date, \pageref{`@lastpage'} pages\end{center}
\vskip \baselineskip}
\def\endabstract{\vskip 1.5\baselineskip \par \noindent Keywords: \@keywords
\gdef\@keywords{}\let\keywords\relax
\gdef\@shortdegree{}\let\shortdegree\relax
\gdef\@department{}\let\department\relax
\gdef\@supervisor{}\let\supervisor\relax
\gdef\@cosupervisor{}\let\cosupervisor\relax
\gdef\@authoruppercase{}\let\authoruppercase\relax
\if@restonecol \twocolumn\fi\oneandhalfspacing\newpage }

\def\dedication#1{
\begin{center}\null
%\addtocontents{toc}{DEDICATION}
\vspace*{\fill}#1
%\addcontentsline{toc}{leads}{\protect\numberline {}DEDICATION}
\vspace*{\fill}
\end{center}
\newpage}

\def\plagiarism{
\null
\vspace*{\fill}
\vspace*{\fill}
\vspace*{\fill}
\vspace*{\fill}
\vspace*{\fill}
\parindent0cm
\singlespacing {\bf I hereby declare that all information in this document has been obtained and presented in accordance with academic rules and ethical conduct. I also declare that, as required by these rules and conduct, I have fully cited and referenced all material and results that are not original to this work.}\\[1.5cm]
\begin{flushleft}
\hfill{
\begin{tabular}{l@{}ll}
\textnormal{Name, Last Name} & \textnormal{:} & \textnormal\bf{\@authoruppercase} \\[1cm]
\textnormal{Signature} & \textnormal{:} &
\end{tabular}
}
\vspace*{\fill}
\end{flushleft}
\newpage}

%\newcommand{dedication}[1]
%{
%\begin{center}
%\null
%\addcontentsline{toc}{leads}{\protect\numberline{} DEDICATION}
%#1
%\end{center}
%\newpage
%}

\newenvironment{acknowledgments}{
\@restonecolfalse
\if@twocolumn
    \@restonecoltrue\onecolumn
\fi
\vskip6.5mm
\chapter*{ACKNOWLEDGMENTS} \@mkboth{ACKNOWLEDGMENTS}{ACKNOWLEDGMENTS}
\addcontentsline{toc}{leads}{\protect\numberline{}ACKNOWLEDGMENTS}
}{
\if@restonecol
    \twocolumn
\fi
\doublespacing\newpage
}

%\def\acknowledgments{
%\addcontentsline{toc}{leads}{\protect\numberline {}ACKNOWLEDGMENTS}
%\@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn
%\fi\chapter*{ACKNOWLEDGMENTS}\@mkboth {ACKNOWLEDGMENTS}{ACKNOWLEDGMENTS}}
%\def\endacknowledgments{\if@restonecol \twocolumn\fi\doublespacing\newpage}

\newenvironment{preface}{
\addcontentsline{toc}{leads}{\protect\numberline {}PREFACE}
\@restonecolfalse
\if@twocolumn
    \@restonecoltrue\onecolumn
 \fi
\chapter*{PREFACE}\@mkboth {PREFACE}{PREFACE}
}{
\if@restonecol
    \twocolumn
\fi
\doublespacing\newpage
}

\newenvironment{curriculumvitae}{
\@restonecolfalse
\if@twocolumn
    \@restonecoltrue\onecolumn
\fi
\chapter*{CURRICULUM VITAE} \@mkboth{CURRICULUM VITAE}{CURRICULUM VITAE}
\addcontentsline{toc}{leads}{\protect\numberline{}CURRICULUM VITAE}
}{
\if@restonecol
    \twocolumn
\fi
\doublespacing
}

\def\vita{\addcontentsline{toc}{leads}{\protect\numberline {}VITA}
\@restonecolfalse\if@twocolumn\@restonecoltrue\onecolumn
 \pagestyle{plain}
 \fi\chapter*{VITA}\@mkboth {VITA}{VITA}}
\def\endvita{\if@restonecol \twocolumn\fi\doublespacing}

\def\preliminaries{\pagenumbering{roman}\pagestyle{plain}}%
\def\endpreliminaries{\newpage\pagestyle{headings}\pagenumbering{arabic}%
\addcontentsline{toc}{nodots}{\protect\numberline {}CHAPTERS}%
\addcontentsline{lof}{nodots}{\protect\numberline {}FIGURES}%
\addcontentsline{lot}{nodots}{\protect\numberline {}TABLES}}

\mark{{}{}}

\def\ps@headings{\let\@mkboth\markboth
\def\@oddfoot{}\def\@evenfoot{}%
\ifpn@topr \def\@evenhead{\hbox{} \hfil \rm\thepage}
\else \def\@evenhead{\hbox{} \hfil \rm\thepage \hfil} \fi
\let\@oddhead=\@evenhead%
\def\chaptermark##1{\markboth {\uppercase{\ifnum \c@secnumdepth >\m@ne
 \@chapapp\ \thechapter. \ \fi ##1}}{}}%
\def\sectionmark##1{\markright {\uppercase{\ifnum \c@secnumdepth >\z@
 \thesection. \ \fi ##1}}}}

\def\ps@plain{\let\@mkboth\markboth
\def\@evenfoot{\hbox{} \hfil \rm\thepage \hfil} \let\@oddfoot=\@evenfoot%
\def\@evenhead{} \let\@oddhead=\@evenhead%
\def\chaptermark##1{\markboth {\uppercase{\ifnum \c@secnumdepth >\m@ne
 \@chapapp\ \thechapter. \ \fi ##1}}{}}%
\def\sectionmark##1{\markright {\uppercase{\ifnum \c@secnumdepth >\z@
 \thesection. \ \fi ##1}}}}

\def\ps@myheadings{\let\@mkboth\@gobbletwo
\def\@oddhead{\hbox{}\sl\rightmark \hfil
\rm\thepage}\def\@oddfoot{}\def\@evenhead{\rm \thepage\hfil\sl\leftmark\hbox
{}}\def\@evenfoot{}\def\sectionmark##1{}\def\subsectionmark##1{}}

\def\today{\ifcase\month\or
 January\or February\or March\or April\or May\or June\or
 July\or August\or September\or October\or November\or December\fi
 \space\number\space\year}

\def\thisday{\ifcase\month\or
 January\or February\or March\or April\or May\or June\or
 July\or August\or September\or October\or November\or December\fi
 \space\number\day, \number\year}

\def\bugun{\ifcase\month\or
 Ocak\or \c{S}ubat\or Mart\or Nisan\or May{\i}s\or Haziran\or
 Temmuz\or A\u{g}ustos\or Eyl\"{u}l\or Ekim\or Kas{\i}m\or Aral{\i}k\fi
 \space\number\year}

\let\@date\today
\let\@tdate\bugun

\@addtoreset{equation}{chapter}
\def\theequation{\thechapter.\arabic{equation}}

\ifpn@top \pagestyle{headings} \else \pagestyle{plain} \fi
\pagenumbering{arabic} \onecolumn

\textheight=23.5cm

\uccode`@=`I \lccode`@="10

\let\@enddoc=\enddocument
\def\enddocument{\label{`@lastpage'}\@enddoc }


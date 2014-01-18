LATEX = pdflatex
BIBTEX = bibtex

RERUN = "(There were undefined references|Rerun to get (cross-references|the bars) right)"

doc = thesis
bib = thesis_references
chapters = introduction/introduction.tex

all: $(doc).pdf

$(doc).pdf $(doc).aux : $(doc).tex $(doc).bbl $(chapters)
	pdflatex $(doc).tex
	egrep $(RERUN) $(doc).log && pdflatex $(doc).tex || true

$(doc).bbl : $(bib).bib $(doc).aux
	bibtex $(doc)

clean:
	rm $(doc).aux $(doc).bbl $(doc).blg $(doc).lof $(doc).log $(doc).toc $(doc).lot 

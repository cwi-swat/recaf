TARGETS      := paper.pdf
PDFLATEX     := $$pdflatex='pdflatex -interaction=nonstopmode -synctex=1 %S %O'
LATEXMK      := latexmk -cd -e $(PDFLATEX) -f -pdf

all : $(TARGETS)

%.pdf : %.tex paper.bib
	$(LATEXMK) $<

.PHONY : all

clean :	
	latexmk -C
	
.PHONY : clean

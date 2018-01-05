# RobotWars
Projet programmation avancée

Everything is on the doc :D

- Mécanisme de gestion de plugins et de chargement dynamique

    + Chaque plugin est instancié à la main (et prochainement dynamiquement via les annotations)

    + Le mécanisme de plugins utilise une annotation PluginTrait détaillant l'action exécutée / ses dépendances

- Persistance (sauvegarde de l'état des plugins)
    + Chaque plugin est compilé par maven en .class (configuration Plugins)

- Modularité et dépendances
    + Le projet est un Maven multi-project, aucune dépendance externe à Java / Maven n'est utilisée

- Documentation / gestion du projet 
// TODO

[![forthebadge](http://forthebadge.com/images/badges/made-with-crayons.svg)](http://forthebadge.com)

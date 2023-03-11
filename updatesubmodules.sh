git submodule update --init --recursive

BASE=$(pwd)

# update LS
cd LobbyService
git branch
git checkout master
git pull

cd $BASE
